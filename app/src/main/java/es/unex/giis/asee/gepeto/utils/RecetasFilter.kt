package es.unex.giis.asee.gepeto.utils

import es.unex.giis.asee.gepeto.model.Receta

interface RecetasFilter {
    fun getRecetasIntf(): List<Receta>
}