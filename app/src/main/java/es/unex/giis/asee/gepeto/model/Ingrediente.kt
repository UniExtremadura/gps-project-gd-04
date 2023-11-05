package es.unex.giis.asee.gepeto.model

import java.io.Serializable

data class Ingrediente(
val idIngrediente: String,
    val nombre: String,
    val descripcion: String,
) : Serializable {

}