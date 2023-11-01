package es.unex.giis.asee.gepeto.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import java.util.TreeSet

fun filtrarLista ( buscador: EditText, itemSet: TreeSet<String>, adapter: ItemSwapAdapter ) {
    buscador.addTextChangedListener( object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString().trim()
            val listaFiltrada = itemSet.filter {
                it.contains(text, ignoreCase = true)
            }
            adapter.swap(TreeSet<String>(listaFiltrada))
        }

        override fun afterTextChanged(s: Editable?) {

        }

    } )
}