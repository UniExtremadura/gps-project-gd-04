package es.unex.giis.asee.gepeto.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.TodoAdapter
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.utils.SwapElementsFilter
import es.unex.giis.asee.gepeto.utils.getElementosFiltrados
import java.util.TreeSet

class ListaViewModel (
    homeViewModel: HomeViewModel
) : ViewModel(), SwapElementsFilter {

    private var listaIngredientes: TreeSet<String> = getElementosFiltrados(
        homeViewModel.ingredientesSeleccionados,
        TreeSet(todosLosIngredientes)
    )

    var seleccionados : TodoAdapter? = null
    var todosIngredientes : ItemSwapAdapter? = null

    override fun getElements(): TreeSet<String> {
        return listaIngredientes
    }

    fun onClickSeleccionados(ingrediente: String) {
        seleccionados!!.check(ingrediente)
    }

    fun onLongClickSeleccionados(ingrediente: String) {
        seleccionados!!.remove(ingrediente)

        listaIngredientes.add(ingrediente)
        todosIngredientes!!.add(ingrediente)
    }

    fun onClickTodos(ingrediente: String) {
        seleccionados!!.add(ingrediente)
        listaIngredientes.remove(ingrediente)
        todosIngredientes!!.remove(ingrediente)
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
                return ListaViewModel(
                    (application as GepetoApplication)
                        .appContainer.homeViewModel,
                ) as T
            }
        }
    }
}
