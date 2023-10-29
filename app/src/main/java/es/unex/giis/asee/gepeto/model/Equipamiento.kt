package es.unex.giis.asee.gepeto.model

import java.io.Serializable

class Equipamiento (
    val id: Int,
    val nombre: String
) : Serializable {

    override fun toString(): String {
        return nombre
    }
}