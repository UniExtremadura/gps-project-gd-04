package es.unex.giis.asee.gepeto.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.utils.SwapElementsFilter
import es.unex.giis.asee.gepeto.utils.getElementosFiltrados
import java.util.TreeSet

class IngredientesViewModel (
    private val homeViewModel: HomeViewModel
) : ViewModel(), SwapElementsFilter {

    var seleccionados : ItemSwapAdapter? = null
    var todosIngredientes : ItemSwapAdapter? = null

    // Esta lista almacenará todos los cambios que se hagan en la lista de todos los ingredientes
    private var listaIngredientes = getElementosFiltrados(
        homeViewModel.ingredientesSeleccionados,
        TreeSet(todosLosIngredientes)
    )
    // Utilizo un treeset porque no admite duplicados y los elementos están ordenados automaticamente

    override fun getElements(): TreeSet<String> {
        return listaIngredientes
    }

    fun onClickSeleccionados(ingrediente: String) {
        homeViewModel.ingredientesSeleccionados.remove(ingrediente)
        seleccionados!!.remove(ingrediente)

        todosIngredientes!!.add(ingrediente)
        listaIngredientes.add(ingrediente)
    }

    fun onClickTodos(ingrediente: String) {
        homeViewModel.ingredientesSeleccionados.add(ingrediente)
        seleccionados!!.add(ingrediente)

        todosIngredientes!!.remove(ingrediente)
        listaIngredientes.remove(ingrediente)
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
                return IngredientesViewModel(
                    (application as
                            GepetoApplication).appContainer.homeViewModel,
                ) as T
            }
        }
    }
}