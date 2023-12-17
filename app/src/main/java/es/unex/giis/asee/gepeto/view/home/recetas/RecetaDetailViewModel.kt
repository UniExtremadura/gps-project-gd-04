package es.unex.giis.asee.gepeto.view.home.recetas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.toEquipamiento
import es.unex.giis.asee.gepeto.data.toRecipe
import es.unex.giis.asee.gepeto.model.Equipamiento
import es.unex.giis.asee.gepeto.model.Pasos
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.launch
import java.util.Locale

class RecetaDetailViewModel (
    private val repository: Repository
) : ViewModel() {

    var descriptionText: MutableLiveData<String> = MutableLiveData<String>("")
    var equipmentText: MutableLiveData<String> = MutableLiveData<String>("")

    private val _recetaDetail = MutableLiveData<Receta>(null)
    val recetaDetail: LiveData<Receta>
        get() = _recetaDetail
    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    var user: User? = null

    var receta: Receta? = null
        set(value) {
            field = value
            getReceta()
        }

    private lateinit var steps: List<Pasos>
    private lateinit var equipments: List<Equipamiento>

    private fun getReceta() {
        if (receta != null)
            viewModelScope.launch{
                try{
                    _recetaDetail.value = receta!!

                    //Descripcion
                    if(receta!!.descripcion.isEmpty()){
                        steps = fetchPasos().filterNotNull().map { it.toRecipe() } // Pasos

                        descriptionText.value = steps.flatMap { it.descripcion }.joinToString("\n\n - ", prefix = "Pasos:\n\n - ")
                    }
                    else{
                        descriptionText.value = receta!!.descripcion
                    }

                    //Equipamientos
                    if(receta!!.equipamientos.isEmpty()){
                        equipments = listOf(fetchEquipamiento().toEquipamiento())

                        equipmentText.value = equipments.flatMap { equipamiento ->
                            equipamiento.descripcion.map {
                                    desc ->
                                desc.trim()
                                    .replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                    }
                            }
                        }.joinToString("\n\n - ", prefix = "Equipamiento:\n\n - ")
                    }
                    else{
                        equipmentText.value = receta!!.listaEquipamiento()
                    }

                } catch (error: APIError) {
                    _toast.value = error.message
                }
            }
    }
    fun setFavorite(receta: Receta){
        viewModelScope.launch {
            receta.favorita = true
            repository.updateReceta(receta,user!!.userId!!)
        }
    }
    fun setNoFavorite(receta: Receta){
        viewModelScope.launch {
            receta.favorita = false
            repository.updateReceta(receta,user!!.userId!!)
        }
    }

    private suspend fun fetchPasos(): Instructions {
        val pasos: Instructions
        try {
            // Utiliza la letra aleatoria en la llamada a la API
            //pasos = getNetworkService().getMealSteps(receta.recetaId.toString())
            pasos = repository.getMealSteps(receta!!.recetaId.toString())

        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return pasos
    }

    private suspend fun fetchEquipamiento(): Equipments {
        val equipamiento : Equipments
        try {
            //equipamiento = getNetworkService().getMealEquipments(receta.recetaId.toString())
            equipamiento = repository.getMealEquipments(receta!!.recetaId.toString())

            if (equipamiento.equipment.isEmpty()){
                throw APIError("No hay equipamiento para esta receta", null)
            }

        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos del equipamiento", cause)
        }

        return equipamiento
    }

    fun onToastShown() {
        _toast.value = null
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
// Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                return RecetaDetailViewModel(
                    (application as GepetoApplication).appContainer.repository,
                ) as T
            }
        }
    }
}