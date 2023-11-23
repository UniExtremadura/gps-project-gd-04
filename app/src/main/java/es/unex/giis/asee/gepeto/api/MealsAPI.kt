package es.unex.giis.asee.gepeto.api

import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.data.api.Instructions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val apikey = "apiKey=01056f554f044a46ac4b0a23c19fc6ba"

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

    // Devuelve los pasos de una receta
    //GET https://api.spoonacular.com/recipes/{id}/analyzedInstructions
    @GET("{id}/analyzedInstructions?"+ apikey)
    suspend fun getMealSteps(@Path("id") id: String): Instructions

    //GET https://api.spoonacular.com/recipes/1003464/equipmentWidget.json
    @GET("{id}/equipmentWidget.json?" + apikey)
    suspend fun getMealEquipments(@Path("id") id: String): Equipments

}
class APIError(message: String, cause: Throwable?) : Throwable(message, cause)
