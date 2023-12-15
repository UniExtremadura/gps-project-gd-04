package es.unex.giis.asee.gepeto.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import es.unex.giis.asee.gepeto.model.Receta
import java.util.TreeSet


fun ocultarBottomNavigation ( view: View, bottomNavigationView: BottomNavigationView ) {
    view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            // Calcula la diferencia entre la altura original y la altura actual
            val heightDiff = view.rootView.height - view.height

            // Si la diferencia es significativa, es probable que el teclado esté en pantalla
            val isKeyboardOpen = heightDiff > view.rootView.height / 6

            // Oculta o muestra la barra de navegación inferior según sea necesario
            if (isKeyboardOpen) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }

            // Devuelve true para continuar con la animación
            return true
        }
    })
}

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

fun filtrarRecetas(buscador: EditText, recetas: List<Receta>, adapter: RecetasAdapter) {
    buscador.addTextChangedListener( object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString().trim()
            val listaFiltrada = recetas.filter {
                Log.w("Filtrando", it.nombre)
                it.nombre.contains(text, ignoreCase = true)
            }
            adapter.updateData(listaFiltrada)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    } )
}

/**
 * Devuelve true si existe intersección entre las dos listas
 * @param lista1 Lista de elementos
 * @param lista2 Lista de elementos
 * @return true si existe intersección entre las dos listas
 */
fun existeInterseccion(lista1: List<Any>, lista2: List<Any>): Boolean {
    var interseccion = false
    for (elemento in lista1) {
        if (lista2.contains(elemento)) {
            interseccion = true
            break
        }
    }
    return interseccion
}
