package es.unex.giis.asee.gepeto.model

import java.io.Serializable

data class Receta(
    val idReceta: String,
    val nombre: String,
    val descripcion: List<Pasos>,
    var favorita: Boolean,
    val ingredientes: List<String> = emptyList(),
    val equipamientos: List<String> = emptyList(),
    val imagen: Int,
    val imagenPath: String
) : Serializable {
    fun getIngredientes(): String {
        //Filtramos los ingredientes que no estén vacíos
        val ingredientesNoVacios = ingredientes.filter { it.isNotBlank() }

        //Si no hay ingredientes, devolvemos un mensaje
        return if (ingredientesNoVacios.isEmpty()) {
            "No hay ingredientes."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            ingredientesNoVacios.joinToString(
                separator = ", ",
                postfix = ".",
                prefix = "Ingredientes: ",
            )
        }
    }
    fun listaIngredientes(): String {
        //Filtramos los ingredientes que no estén vacíos
        val ingredientesNoVacios = ingredientes.filter { it.isNotBlank() }

        //Si no hay ingredientes, devolvemos un mensaje
        return if (ingredientesNoVacios.isEmpty()) {
            "No hay ingredientes."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            "Ingredientes:\n\n - " + ingredientesNoVacios.joinToString(
                separator = "\n - "
            )
        }
    }

    fun listaEquipamiento(): String {
        //Filtramos los ingredientes que no estén vacíos
        val equipamientoNoVacios = equipamientos.filter { it.isNotBlank() }

        //Si no hay ingredientes, devolvemos un mensaje
        return if (equipamientoNoVacios.isEmpty()) {
            "No hay equipamiento."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            "Equipamiento / Medidas:\n\n - " + equipamientoNoVacios.joinToString(
                separator = "\n - "
            )
        }
    }

    fun showDescripcion(): String {

        return if (descripcion.isEmpty()) {
            "No hay descripción."
        } else {
            "Descripción:\n\n$descripcion"
        }
    }
}