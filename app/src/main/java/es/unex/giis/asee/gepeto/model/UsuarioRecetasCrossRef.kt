package es.unex.giis.asee.gepeto.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["userId", "recetaId"],
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["recetaId"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioRecetasCrossRef (
    val userId : Long,
    val recetaId : Int
)