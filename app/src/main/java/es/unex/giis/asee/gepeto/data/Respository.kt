package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.MealsAPI
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.data.api.RecipesItem
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef
import es.unex.giis.asee.gepeto.utils.Tuple
import es.unex.giis.asee.gepeto.utils.existeInterseccion
import kotlin.random.Random

class Repository (
    private val userDao: UserDao,
    private val recetaDao: RecetaDao,
    private val networkService: MealsAPI
) {
    private var lastUpdateTimeMillis: Long = 0L
    val recetas = recetaDao.getAllRecetas()

    suspend fun recipeToLibrary(receta: Receta, userId: Long) {
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
    suspend fun getHistorial(userId: Long): List<Receta> {
        return recetaDao.getUserConRecetas(userId).recetas
    }

    suspend fun tryUpdateRecentRecipesCache() {
        if (shouldUpdateShowsCache()) {
            if (Session.exists("cache")) {
                val cache = Session.getValue("cache") as Tuple<Recipes, List<String>>
                fetchRecentRecipe(cache.second)
            }
        }
    }

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
            //recipes = getNetworkService().getMealByIngredients(ingredients = ingredientesString)
            recipes = networkService.getMealByIngredients(ingredients = ingredientesString)

        } catch (cause: Throwable) {
            throw APIError("No existe una receta con esos ingredientes!", cause)
        }

        return recipes
    }

    suspend fun fetchRecentRecipe( ingredientes: List<String> ): Receta {
        try {
            lastUpdateTimeMillis = System.currentTimeMillis()
            var recipes: Recipes

            if (Session.exists("cache")) {
                val cache = Session.getValue("cache") as Tuple<Recipes, List<String>>
                recipes = cache.first
                val listaIngredientes = cache.second

                if ( recipes.isNotEmpty() ) {
                    if (existeInterseccion(ingredientes, listaIngredientes)) {
                        return recipes.removeAt(0).toRecipe()
                    }
                }
            }

            recipes = fetchMeal(ingredientes)
            Session.setValue("cache", Tuple(recipes, ingredientes))
            return recipes.removeAt(0).toRecipe()

        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
        }
    }

    private fun shouldUpdateShowsCache(): Boolean {
        var numeroDeRecetas = 0
        if (Session.exists("cache")) {
            val cache = Session.getValue("cache") as Tuple<Recipes, List<String>>
            numeroDeRecetas = cache.first.size
        }
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || numeroDeRecetas == 0
    }

    suspend fun findUserByName(username: String): User {
        return userDao.findByName(username)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}