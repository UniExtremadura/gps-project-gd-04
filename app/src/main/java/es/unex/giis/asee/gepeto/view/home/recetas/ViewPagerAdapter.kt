import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.navigation.fragment.findNavController
import es.unex.giis.asee.gepeto.view.home.recetas.FavoritasFragment
import es.unex.giis.asee.gepeto.view.home.recetas.RecetasFragment
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.view.home.recetas.HistorialFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2 // Número de pestañas
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistorialFragment()
            1 -> FavoritasFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    // Agrega esta función para obtener el título de la pestaña en función de la posición
    fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Historial"
            1 -> "Favoritas"
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    // Esta función te permitirá configurar la acción de navegación desde el fragmento seleccionado
    fun navigateToFragment(position: Int, fragment: Fragment) {
        val navController = fragment.findNavController()
        when (position) {
            0 -> {
                // Configura la acción de navegación para RecetasFragment
                navController.navigate(R.id.action_recetasFragment_to_favoritasFragment)
            }
            1 -> {
                // Configura la acción de navegación para FavoritasFragment
                navController.navigate(R.id.action_recetasFragment_to_historialFragment)
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
