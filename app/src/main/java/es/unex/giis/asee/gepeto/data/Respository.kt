package es.unex.giis.asee.gepeto.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.MealsAPI
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.database.dao.RecetaCacheDao
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class Repository (
    private val userDao: UserDao,
    private val recetaDao: RecetaDao,
    private val networkService: MealsAPI,
    private val recetaCacheDao: RecetaCacheDao,
) {
//    private var lastUpdateTimeMillis: Long = 0L

    private var _receta = MutableLiveData<Receta?>()
    val receta: LiveData<Receta?>
        get() = _receta

    suspend fun updateReceta(receta: Receta, userId: Long) {
        recetaDao.update(receta)
        recetaDao.insertUsuarioReceta(UsuarioRecetasCrossRef(userId, receta.recetaId!!))
    }

    suspend fun insertAndRelate(recipe: Receta, userId: Long) {
        recetaDao.insertAndRelate(recipe, userId)
    }

        // Función que devuelve las recetas FAVORITAS
    suspend fun getFavoritas(userId: Long): List<Receta> {
        return recetaDao.getUserConRecetas(userId).recetas.filter { it.favorita }
    }

    // Función que devuelve las recetas HISTORIAL
    suspend fun getRecetas(userId: Long): List<Receta> {
        return recetaDao.getUserConRecetas(userId).recetas
    }

    suspend fun getRecetaById(recetaId: Int): Receta {
        return recetaDao.findById(recetaId)
    }

//    suspend fun tryUpdateRecentRecipesCache() {
//        if (shouldUpdateShowsCache()) {
//            if (Session.exists("cache")) {
//                val cache = Session.getValue("cache") as Tuple<Recipes, List<String>>
//                fetchRecentRecipe(cache.second)
//            }
//        }
//    }

    suspend fun getMealSteps(id: String): Instructions {
        return networkService.getMealSteps(id)
    }

    suspend fun getMealEquipments(id: String): Equipments {
        return networkService.getMealEquipments(id)
    }

    private suspend fun fetchMeal( ingredientes: List<String> ): Recipes {
        val recipes: Recipes
        try {
            //crear una variable Ingredientes que será un string que contenga todos los valore de args.ingredientes separados por un ",+"
            val ingredientesString = ingredientes.joinToString(",+")

            // Utiliza los ingredientes para buscar 1 receta
            recipes = networkService.getMealByIngredients(ingredients = ingredientesString)

        } catch (cause: Throwable) {
            throw APIError("No existe una receta con esos ingredientes!", cause)
        }

        return recipes
    }

    suspend fun fetchRecentRecipe( ingredientes: List<String> ) {
        try {
//            lastUpdateTimeMillis = System.currentTimeMillis()

            val ingredientesQuery = ingredientes.joinToString(";")
            val recetaCache = recetaCacheDao.getAndDelete(ingredientesQuery)
            if (recetaCache != null) {
                _receta.value = recetaCache.toReceta()
                return // No need to fetch from network
            }

            val recipes = fetchMeal(ingredientes)
            recetaCacheDao.insertAll(recipes.map { it.toRecipe().toRecetaCache(ingredientesQuery)})
            _receta.value = recetaCacheDao.getAndDelete(ingredientesQuery)!!.toReceta()

        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
        }
    }

    suspend fun fetchAIRecipe(ingredientes: List<String>, equipamientos: List<Any>, observaciones : String) {
        val openAI = OpenAI(
            token = "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",
            timeout = Timeout(socket = 60.seconds)
        )

        try {
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content="Generate a recipe based on the provided list of ingredients, kitchen equipment, and observations. " +
                                "The recipe must have only the five following attributes, each separated by '%%%' (there should be only 4 '%%%'):\n"+
                                "1.name\n"+
                                "2.steps\n"+
                                "3.ingredients (only their names and separated by ';')\n"+
                                "4.equipment (only their names and separated by ';')\n"+
                                "5.image url of the recipe\n"+
                                "It must have the format of the following example:\n"+
                                "Omelette %%% \n1. Use eggs\n2.Create Omelette \n%%% Apple;Amaretto %%% Blender;Oven %%% https://spoonacular.com/recipeImages/650871-312x231.jpg\n"+

                                "Ensure that the generated recipe includes only the specified sections and does not include any additional text " +
                                "beyond the given five sections."+
                                "Use the provided arrays for ingredients, equipment and observations:\n"+
                                "Ingredients: ${ingredientes.joinToString(separator = ", ")}\n" +
                                "Equipament: ${equipamientos.joinToString(separator = ", ")}\n" +
                                "Observations: $observaciones\n"
                    )
                )
            )

            val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

            val response = completion.choices.first().message?.content
            print(response)
            val textResultArray=response?.split("%%%") ?: listOf("","","","")

           val images = openAI.imageURL(
                creation = ImageCreation(
                    prompt = textResultArray[0],
                    n = 1,
                    size = ImageSize.is1024x1024
                )
            )

            _receta.value = Receta(
                recetaId = Random.nextInt(1000),
                nombre = textResultArray[0].replace("\n", ""),
                descripcion = textResultArray[1],
                favorita = false,
                ingredientes = textResultArray[2].replace("\n", ""),
                equipamientos = textResultArray[3].replace("\n", ""),
                imagen = R.drawable.ejemplo_plato,
                //imagenPath = textResultArray[4].replace("\n", ""),
                imagenPath = images[0].url
            )

        } catch (e: Exception) {
            Log.v("GPT_Petition", "getGPTResponse: ERROR: ${e.message ?: ""}")
        }
    }

    fun onRecetaSent() {
        _receta.value = null
    }

//    private fun shouldUpdateShowsCache(): Boolean {
//        var numeroDeRecetas = 0
//        if (Session.exists("cache")) {
//            val cache = Session.getValue("cache") as Tuple<Recipes, List<String>>
//            numeroDeRecetas = cache.first.size
//        }
//        val lastFetchTimeMillis = lastUpdateTimeMillis
//        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
//        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || numeroDeRecetas == 0
//    }

    suspend fun findUserByName(username: String): User {
        return userDao.findByName(username)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

//    companion object {
//        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
//    }
}