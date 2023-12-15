package es.unex.giis.asee.gepeto.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.launch
import java.util.TreeSet

class ObservacionesViewModel (
    private val repository: Repository
) : ViewModel() {

    var receta = MutableLiveData<Receta>(null)

    var user : User? = null

    var ingredientesText = ""
    private var ingredientesList = MutableLiveData<TreeSet<String>>(null)

    var equipamientoText = MutableLiveData<String>("")
    var equipamientoList = TreeSet<String>()

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    fun setAttributes() {
        equipamientoText.value = equipamientoList.joinToString(separator = ", ", postfix = ".")
        ingredientesList.value = TreeSet(ingredientesText.removeSuffix(".").split(", "))
    }

    fun generarReceta() {
        viewModelScope.launch {
            try {
                val recipe = repository.fetchRecentRecipe(ingredientesList.value!!.toList())
                repository.insertAndRelate(recipe, user!!.userId!!)
                receta.value = recipe

            } catch (e: APIError) {
                _toast.value = e.message
            }
        }
    }

    fun onToastShown() {
        _toast.value = null
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
                return ObservacionesViewModel(
                    (application as
                            GepetoApplication).appContainer.repository,
                ) as T
            }
        }
    }
}