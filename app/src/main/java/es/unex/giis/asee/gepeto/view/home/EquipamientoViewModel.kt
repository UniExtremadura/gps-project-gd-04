package es.unex.giis.asee.gepeto.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import es.unex.giis.asee.gepeto.utils.SwapElementsFilter
import es.unex.giis.asee.gepeto.utils.getElementosFiltrados
import java.util.TreeSet

class EquipamientoViewModel (
    private val homeViewModel: HomeViewModel
) : ViewModel(), SwapElementsFilter {

    private var listaEquipamiento: TreeSet<String> = getElementosFiltrados(
        homeViewModel.equipamientoSeleccionado,
        TreeSet(equipamientosDeCocina)
    )

    var seleccionados : ItemSwapAdapter? = null
    var todosEquipamiento : ItemSwapAdapter? = null

    fun onClickSeleccionados(equipamiento: String) {
        homeViewModel.equipamientoSeleccionado.remove(equipamiento)
        seleccionados!!.remove(equipamiento)

        todosEquipamiento!!.add(equipamiento)
        listaEquipamiento.add(equipamiento)
    }

    fun onClickTodos(equipamiento: String) {
        homeViewModel.equipamientoSeleccionado.add(equipamiento)
        seleccionados!!.add(equipamiento)

        todosEquipamiento!!.remove(equipamiento)
        listaEquipamiento.remove(equipamiento)
    }

    override fun getElements(): TreeSet<String> {
        return listaEquipamiento
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object :
            ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return EquipamientoViewModel(
                    (application as GepetoApplication)
                        .appContainer.homeViewModel,
                ) as T
            }
        }
    }
}