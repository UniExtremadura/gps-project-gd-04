package es.unex.giis.asee.gepeto.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Locale

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
            val ingredientesConMayuscula = ingredientesNoVacios.map { ingrediente ->
                ingrediente.trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }

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
            val ingredientesConMayuscula = ingredientes.split(";").map { ingrediente ->
                ingrediente.trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }

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
}