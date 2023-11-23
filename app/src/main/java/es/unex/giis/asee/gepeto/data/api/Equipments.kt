package es.unex.giis.asee.gepeto.data.api

import com.google.gson.annotations.SerializedName

class Equipments (
    @SerializedName("equipment" ) var equipment : ArrayList<Equipment> = arrayListOf()
)
