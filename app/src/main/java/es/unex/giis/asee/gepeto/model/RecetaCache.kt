package es.unex.giis.asee.gepeto.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class RecetaCache (
    @PrimaryKey(autoGenerate = true) val recetaId: Int?,
    val nombre: String,
    val ingredientes: String,
    @ColumnInfo(name = "query_param") val queryParam: String,
    @ColumnInfo(name = "imagen_path") val imagenPath: String
) : Serializable {
    fun toReceta(): Receta {
        return Receta(
            recetaId,
            nombre,
            "",
            false,
            ingredientes,
            "",
            0,
            imagenPath
        )
    }
}