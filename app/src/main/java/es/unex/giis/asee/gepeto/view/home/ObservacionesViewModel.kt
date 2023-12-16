package es.unex.giis.asee.gepeto.view.home

import android.os.SystemClock.sleep
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

    val receta = repository.receta

    var user : User? = null

    var ingredientesText = ""
    private var ingredientesList = MutableLiveData<TreeSet<String>>(null)

    var equipamientoText = MutableLiveData("")
    var equipamientoList = TreeSet<String>()

    var observaciones = ""

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    fun setAttributes() {
        equipamientoText.value = equipamientoList.joinToString(separator = ", ", postfix = ".")
        ingredientesList.value = TreeSet(ingredientesText.removeSuffix(".").split(", "))
    }

    fun vincularRecetaConUsuario(receta: Receta) {
        viewModelScope.launch {
            repository.insertAndRelate(receta, user!!.userId!!)
        }
    }

    fun generarReceta() {
        viewModelScope.launch {
            try {
                repository.fetchRecentRecipe(ingredientesList.value!!.toList())

            } catch (e: APIError) {
                _toast.value = e.message
            }
        }
    }

    fun generarRecetaIA() {
        viewModelScope.launch {
            try {
                _toast.value = "Generando receta, puede tardar unos segundos..."
                repository.fetchAIRecipe(ingredientesList.value!!.toList(), equipamientoList.toList(), observaciones)

            } catch (e: APIError) {
                _toast.value = e.message
            }
        }
    }

    fun onRecetaSent() {
        repository.onRecetaSent()
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