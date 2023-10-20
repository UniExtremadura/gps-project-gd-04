package es.unex.giis.asee.gepeto.model

import java.io.Serializable

data class Receta(
    val nombre: String,
    val descripcion: String,
    var favorita: Boolean,
    val ingredientes: List<String> = emptyList(),
    val equipamientos: List<String> = emptyList(),
    val imagen: Int
) : Serializable {
    fun getIngredientes(): String {
        return ingredientes.joinToString(
            separator = ", ", // Delimitador entre los elementos (coma y espacio en este caso)
            prefix = "Ingredientes: ", // Prefijo para la cadena completa
            postfix = "." // Sufijo para la cadena completa
        )
    }
}