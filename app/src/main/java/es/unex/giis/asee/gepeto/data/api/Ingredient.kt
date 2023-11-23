package es.unex.giis.asee.gepeto.data.api
import com.google.gson.annotations.SerializedName


data class Ingredient (

    @SerializedName("idIngredient"   ) var idIngredient   : String? = null,
    @SerializedName("strIngredient"  ) var strIngredient  : String? = null,
    @SerializedName("strDescription" ) var strDescription : String? = null,
    @SerializedName("strType"        ) var strType        : String? = null

)