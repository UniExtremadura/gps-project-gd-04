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
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.utils.RecetasFilter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritasViewModel (
    private val repository: Repository
) : ViewModel(), RecetasFilter {
    var user : User? = null
    var favoritas : List<Receta> = emptyList()
    var adapter : RecetasAdapter? = null

    override fun getRecetasIntf(): List<Receta> {
        return favoritas
    }

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast
    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _buscador = MutableLiveData<Boolean>(false)
    val buscador: LiveData<Boolean>
        get() = _buscador

    private val _noHayFavoritasMessage = MutableLiveData<Boolean>(true)
    val noHayFavoritasMessage: LiveData<Boolean>
        get() = _noHayFavoritasMessage

    init {
        refresh()
    }

    fun refresh() {
        user?.let {
            launchDataLoad {
                favoritas = repository.getRecetas(user!!.userId!!).filter { it.favorita }
                if (favoritas.isEmpty()) {
                    _noHayFavoritasMessage.value = true
                    _buscador.value = false
                } else {
                    _noHayFavoritasMessage.value = false
                    _buscador.value = true
                }
                adapter?.updateData(favoritas)
            }
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

    fun unFavReceta(receta: Receta) {

        if (!receta.favorita) return

        viewModelScope.launch {
            receta.favorita = false
            repository.updateReceta(receta, user!!.userId!!)
            _toast.value = "Receta eliminada de favoritos!"
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
                return FavoritasViewModel(
                    (application as
                            GepetoApplication).appContainer.repository,
                ) as T
            }
        }
    }
}