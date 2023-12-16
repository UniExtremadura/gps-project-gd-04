package es.unex.giis.asee.gepeto.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Receta(
    @PrimaryKey(autoGenerate = true) val recetaId: Int?,
    val nombre: String,
    val descripcion: String,
    @ColumnInfo( name = "es_favorita" ) var favorita: Boolean,
    val ingredientes: String,
    val equipamientos: String,
    val imagen: Int,
    @ColumnInfo( name = "image_path" ) val imagenPath: String
) : Serializable {

    fun toRecetaCache(queryParam: String): RecetaCache {
        return RecetaCache(
            recetaId,
            nombre,
            ingredientes,
            queryParam,
            imagenPath
        )
    }

    fun getIngredientesPreview(): String {
        //Filtramos los ingredientes que no estén vacíos
        val ingredientesNoVacios = ingredientes.split(';')

        //Si no hay ingredientes, devolvemos un mensaje
        return if (ingredientesNoVacios.isEmpty()) {
            "No hay ingredientes."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            val ingredientesConMayuscula = ingredientesNoVacios.map { it.trim().capitalize() }

            ingredientesConMayuscula.joinToString(
                separator = ", ",
                postfix = ".",
                prefix = "Ingredientes: "
            )
        }
    }
    fun listaIngredientesDetalles(): String {

        //Si no hay ingredientes, devolvemos un mensaje
        return if (ingredientes.isEmpty()) {
            "No hay ingredientes."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            val ingredientesConMayuscula = ingredientes.split(";").map { it.trim().capitalize() }

            "Ingredientes:\n\n - " + ingredientesConMayuscula.joinToString(
                separator = "\n - "
            )
        }
    }

    fun listaEquipamiento(): String {

        //Si no hay ingredientes, devolvemos un mensaje
        return if (equipamientos.isEmpty()) {
            "No hay equipamiento."
        } else {
            //Si hay ingredientes, los devolvemos separados por "-"
            "Equipamiento / Medidas:\n\n - " + equipamientos.split(";").joinToString(
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