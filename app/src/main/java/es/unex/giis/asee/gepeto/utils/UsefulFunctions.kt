package es.unex.giis.asee.gepeto.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import java.util.TreeSet


fun filtrarSwapItemElements (buscador: EditText, container: SwapElementsFilter, adapter: ItemSwapAdapter ) {
    buscador.addTextChangedListener( object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString().trim()
            val listaFiltrada = container.getElements().filter {
                it.contains(text, ignoreCase = true)
            }
            adapter.swap(TreeSet<String>(listaFiltrada))
        }

        override fun afterTextChanged(s: Editable?) {

        }

    } )
}

/**
 * Filtra los elementos de todos los elementos según los elementos seleccionados
 * @param elementos Elementos seleccionados
 * @param todosLosElementos Todos los elementos
 * @return Elementos filtrados
 */
fun getElementosFiltrados (elementos: TreeSet<String>, todosLosElementos: TreeSet<String>) : TreeSet<String> {
    val elementosFiltrados = todosLosElementos

    if (elementos.isEmpty()) {
        return elementosFiltrados
    }

    for ( item in elementos ) {
        elementosFiltrados.remove(item)
    }

    return elementosFiltrados
}

/**
 * Filtra las recetas del historial según el texto introducido en el buscador
 * @param buscador EditText del buscador
 * @param container clase que contiene las recetas
 * @param adapter Adaptador de las recetas
 */
fun filtrarRecetasFilter(buscador: EditText, container: RecetasFilter, adapter: RecetasAdapter) {
    buscador.addTextChangedListener( object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString().trim()
            val listaFiltrada = container.getRecetasIntf().filter {
                Log.w("Filtrando", it.nombre)
                it.nombre.contains(text, ignoreCase = true)
            }
            adapter.updateData(listaFiltrada)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    } )
}
