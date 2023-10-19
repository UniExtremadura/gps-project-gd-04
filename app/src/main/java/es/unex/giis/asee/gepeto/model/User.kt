package es.unex.giis.asee.gepeto.model

import java.io.Serializable

data class User(
    val name: String = "",
    val password: String = ""
) : Serializable
