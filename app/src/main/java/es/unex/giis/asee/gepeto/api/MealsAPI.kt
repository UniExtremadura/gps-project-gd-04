package es.unex.giis.asee.gepeto.api

import MealList
import IngredientList
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.data.api.Ingredient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val service: MealsAPI by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MealsAPI::class.java)
}

fun getNetworkService() = service

interface MealsAPI {

    // Devuelve 1 Comida por nombre
    @GET("search.php")
    fun getMealByName(
        @Query("s") mealName: String
    ): Call<Meal>

    // Devuelve una lista de comidas por 1º letra
    @GET("search.php")
    fun getListOfMealsByFirstLetter(
        @Query("f") firstLetter: String
    ): Call<MealList>

    // Devuelve 1 comida por ID
    @GET("lookup.php")
    fun getMealById(
        @Query("i") mealId: String
    ): Call<Meal>

    // Devuelve una lista de comidas por ingrediente principal
    @GET("filter.php")
    fun getMealsByMainIngriedent(
        @Query("i") category: String
    ): Call<MealList>

    // Devuelve una lista de comidas por categoria
    @GET("filter.php")
    fun getMealsByCategory(
        @Query("c") category: String
    ): Call<MealList>

    // Devuelve 1 comida aleatoria
    @GET("random.php")
    fun getRandomMeal(): Call<Meal>

    /*
    // Devuelve todas Categorias
    @GET("categories.php")
    fun getCategories(): Call<CategoriesList>
    */

    //Devuelve todos los ingredientes
    @GET("list.php?i=list")
    fun getIngredientsList(): Call<IngredientList>

}

class APIError(message: String, cause: Throwable?) : Throwable(message, cause)

interface APICallback {
    fun onCompletedMeal(Meal: List<Meal?>)
    fun onError(cause: Throwable)
    fun onSuccessIngredient(Ingredient: List<Ingredient?>)
}