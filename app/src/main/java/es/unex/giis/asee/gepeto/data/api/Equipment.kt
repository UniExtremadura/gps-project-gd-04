package es.unex.giis.asee.gepeto.data.api

import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("name"  ) var name  : String,
    @SerializedName("image" ) var image : String
)
