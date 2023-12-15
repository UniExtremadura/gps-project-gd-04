package es.unex.giis.asee.gepeto.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import java.util.TreeSet

class HomeViewModel : ViewModel() {
    private val _user = MutableLiveData<User>(null)
    val user: LiveData<User>
        get() = _user

    var userInSession: User? = null
        set(value) {
            field = value
            _user.value = value!!
        }

    private val _navigateToReceta = MutableLiveData<Receta>(null)
    val navigateToReceta: LiveData<Receta>
        get() = _navigateToReceta
    fun onRecetaClick(receta: Receta) {
        _navigateToReceta.value = receta
    }

    private val _enviarIngredientes = MutableLiveData<TreeSet<String>>(null)
    val enviarIngredientes: LiveData<TreeSet<String>>
        get() = _enviarIngredientes
    fun onEnviarIngredientesClick(ingredientes: TreeSet<String>) {
        _enviarIngredientes.value = ingredientes
    }

    private val _generarReceta = MutableLiveData<Receta>(null)
    val generarReceta: LiveData<Receta>
        get() = _generarReceta
    fun onGenerarRecetaClick(receta: Receta) {
        _generarReceta.value = receta
    }


    // Seleccionados en Lista, Ingredientes y Equipamiento
    var ingredientesSeleccionados = TreeSet<String>()
    var equipamientoSeleccionados = TreeSet<String>()
    var todoSeleccionados = TreeSet<String>()
}