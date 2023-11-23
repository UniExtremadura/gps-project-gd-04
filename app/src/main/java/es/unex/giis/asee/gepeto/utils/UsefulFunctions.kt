package es.unex.giis.asee.gepeto.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import java.util.TreeSet

fun ocultarBottomNavigation ( view: View, bottomNavigationView: BottomNavigationView ) {
    view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            // Calcula la diferencia entre la altura original y la altura actual
            val heightDiff = view.rootView.height - view.height

            // Si la diferencia es significativa, es probable que el teclado esté en pantalla
            val isKeyboardOpen = heightDiff > view.rootView.height / 4

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