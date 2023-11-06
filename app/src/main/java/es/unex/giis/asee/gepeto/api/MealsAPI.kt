package es.unex.giis.asee.gepeto.api

import MealList
import IngredientList
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.Recipes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val apikey = "apiKey=1541f2b0ab204bc8ab6a8b69be301e86"

private val service: MealsAPI by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/recipes/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MealsAPI::class.java)
}

fun getNetworkService() = service

interface MealsAPI {

    // Devuelve 1 Comida por nombre
    @GET("complexSearch?" + apikey + "&")
    fun getMealByName(@Query("query") name: String): Recipes


    // Devuelve una lista de comidas por 1º letra
    /*
    @GET("complexSearch?" + apikey + "&")
    suspend fun getMealByFirstLetter(@Query("ingredients") letter: String): Recipes
    */

    // Devuelve 1 comida por ID
    /*
    @GET("complexSearch?" + apikey + "&")
    fun getMealById(@Query("query") id: Int): Recipes
    */

    // Devuelve una lista de comidas por ingrediente seleccionados Ej. ingredients=apples,+flour,+sugar
    @GET("findByIngredients?" + apikey + "&")
    suspend fun getMealByIngredients(@Query("ingredients") ingredients: String): Recipes


    // Devuelve una lista de comidas por categoria


    // Devuelve 1 comida aleatoria


    // Devuelve todas Categorias



    //Devuelve todos los ingredientes
    @GET("ingredients/list?" + apikey + "&")
    suspend fun getIngredientsList(): IngredientList


}
class APIError(message: String, cause: Throwable?) : Throwable(message, cause)
