package es.unex.giis.asee.gepeto.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


data class UserConRecetas (
    @Embedded val user : User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "recetaId",
        associateBy = Junction(UsuarioRecetasCrossRef::class)
    )
    val recetas : List<Receta>
)