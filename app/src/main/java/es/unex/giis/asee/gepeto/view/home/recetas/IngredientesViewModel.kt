package es.unex.giis.asee.gepeto.view.home.recetas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.utils.SwapElementsFilter
import es.unex.giis.asee.gepeto.utils.getElementosFiltrados
import es.unex.giis.asee.gepeto.view.home.HomeViewModel
import java.util.TreeSet

class IngredientesViewModel (
    private val repository: Repository
) : ViewModel(), SwapElementsFilter {
    var seleccionados : ItemSwapAdapter? = null
    var todosIngredientes : ItemSwapAdapter? = null
    var homeViewModel : HomeViewModel? = null

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    // Esta lista almacenará todos los cambios que se hagan en la lista de todos los ingredientes
    private var listaIngredientes = TreeSet<String>()
    // Utilizo un treeset porque no admite duplicados y los elementos están ordenados automaticamente

    fun refreshElements() {
        listaIngredientes = getElementosFiltrados(
            homeViewModel!!.ingredientesSeleccionados,
            TreeSet(todosLosIngredientes)
        )
    }

    override fun getElements(): TreeSet<String> {
        return listaIngredientes
    }

    fun onClickSeleccionados(ingrediente: String) {
        todosIngredientes!!.add(ingrediente)
        seleccionados!!.remove(ingrediente)
        listaIngredientes.add(ingrediente)
        homeViewModel!!.ingredientesSeleccionados = seleccionados!!.getSet()
    }

    fun onClickTodos(ingrediente: String) {
        todosIngredientes!!.remove(ingrediente)
        seleccionados!!.add(ingrediente)
        listaIngredientes.remove(ingrediente)
        homeViewModel!!.ingredientesSeleccionados = seleccionados!!.getSet()
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
                            GepetoApplication).appContainer.repository,
                ) as T
            }
        }
    }
}