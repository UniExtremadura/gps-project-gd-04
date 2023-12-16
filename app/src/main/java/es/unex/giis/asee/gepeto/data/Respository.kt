package es.unex.giis.asee.gepeto.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.MealsAPI
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.database.dao.RecetaCacheDao
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.RecetaCache
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef
import es.unex.giis.asee.gepeto.utils.Tuple

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

            val ingredientesQuery = ingredientes[0]
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