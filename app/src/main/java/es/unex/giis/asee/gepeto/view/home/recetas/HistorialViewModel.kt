package es.unex.giis.asee.gepeto.view.home.recetas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HistorialViewModel (
    private val repository: Repository
) : ViewModel() {
    val user : User = Session.getValue("user") as User
    var recetas : List<Receta> = emptyList()
    var adapter : RecetasAdapter? = null

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast
    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _buscador = MutableLiveData<Boolean>(false)
    val buscador: LiveData<Boolean>
        get() = _buscador

    private val _noHayRecetasMessage = MutableLiveData<Boolean>(true)
    val noHayRecetasMessage: LiveData<Boolean>
        get() = _noHayRecetasMessage

    init {
        refresh()
    }

    fun refresh() {
        launchDataLoad {
            recetas = repository.getRecetas(user.userId!!)
            if (recetas.isEmpty()) {
                _noHayRecetasMessage.value = true
                _buscador.value = false
            } else {
                _noHayRecetasMessage.value = false
                _buscador.value = true
            }
            adapter?.updateData(recetas)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                _toast.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }

    fun onToastShown() {
        _toast.value = null
    }

    fun setRecetaFav(receta: Receta) {

        if (receta.favorita) return

        receta.favorita = true
        //db.recetaDao().update(receta)
        viewModelScope.launch {
            repository.recipeToLibrary(receta, user.userId!!)
            _toast.value = "Receta añadida a favoritos!"
        }
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
                return HistorialViewModel(
                    (application as
                            GepetoApplication).appContainer.repository,
                ) as T
            }
        }
    }
}