package es.unex.giis.asee.gepeto.utils

import java.util.TreeSet

interface SwapElementsFilter {
    fun getElements(): TreeSet<String>
}