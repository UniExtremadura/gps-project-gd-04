package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.MealsAPI
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef


class Repository private constructor(
    private val RecetaDao: RecetaDao,
    private val networkService: MealsAPI
) {
    private var lastUpdateTimeMillis: Long = 0L
    val recetas = RecetaDao.getAllRecetas()

    suspend fun recipeToLibrary(receta: Receta, userId: Long) {
        RecetaDao.update(receta)
        RecetaDao.insertUsuarioReceta(UsuarioRecetasCrossRef(userId, receta.recetaId!!))
    }

    suspend fun insertAndRelate(_recipe: Receta, userId: Long) {
        RecetaDao.insertAndRelate(_recipe, userId)
    }


        // Función que devuelve las recetas FAVORITAS
    suspend fun getFavoritas(userId: Long): List<Receta> {
        return RecetaDao.getUserConRecetas(userId).recetas.filter { it.favorita }
    }

    // Función que devuelve las recetas HISTORIAL
    suspend fun getHistorial(userId: Long): List<Receta> {
        return RecetaDao.getUserConRecetas(userId).recetas
    }

    suspend fun tryUpdateRecentShowsCache() {
        if (shouldUpdateShowsCache()) fetchHistorialMeals()
    }

    suspend fun getMealSteps(id: String): Instructions {
        return networkService.getMealSteps(id)
    }

    suspend fun getMealEquipments(id: String): Equipments {
        return networkService.getMealEquipments(id)
    }

    suspend fun getMealByIngredients(ingredientesString: String): Recipes {
        return networkService.getMealByIngredients(ingredients = ingredientesString)
    }

    private suspend fun fetchHistorialMeals() {
        try {
            val user = Session.getValue("user") as User

            val meals = getHistorial(user.userId!!)

            lastUpdateTimeMillis = System.currentTimeMillis()
        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause) }
    }

    private suspend fun shouldUpdateShowsCache(): Boolean {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS //|| RecetaDao.getNumRecetas() == 0L
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        @Volatile private var INSTANCE: Repository? = null

        fun getInstance(
            recetaDao: RecetaDao,
             mealsAPI: MealsAPI
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(recetaDao, mealsAPI).also { INSTANCE = it }
            }
        }
    }
}