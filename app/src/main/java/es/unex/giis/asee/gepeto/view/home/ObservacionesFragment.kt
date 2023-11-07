package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.data.api.RecipesItem
import es.unex.giis.asee.gepeto.data.recetasPrueba
import es.unex.giis.asee.gepeto.data.toShowRecipe
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.utils.BACKGROUND
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import kotlin.random.Random
import java.util.StringJoiner
import java.util.TreeSet


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()

    private lateinit var _binding: FragmentObservacionesBinding

    private var _recipe : Receta? = null

    private val binding get() = _binding
    private lateinit var listener: OnGenerarRecetaListener

    interface OnGenerarRecetaListener {
        fun onGenerarRecetaClick( receta: Receta )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentObservacionesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if ( context is OnGenerarRecetaListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnGenerarRecetaListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val equipamientoSet = Session.getValue("equipamientosSeleccionados") as? TreeSet<String> ?: TreeSet()

        with (binding) {
            ingredientes.text = args.ingredientes

            if (_recipe == null) {
                lifecycleScope.launch {
                    if (_recipe == null){
                        try {

                            _recipe = fetchMeal().toShowRecipe()
                            print(_recipe)
                        } catch (e: APIError) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            equipamiento.text = _recipe?.equipamientos?.joinToString(", ") ?: ""

            crearRecetaBtn.setOnClickListener {
                //val ingredientes = args.ingredientes.removeSuffix(".").split(", ")

                val ingredientes = _recipe?.ingredientes ?: emptyList()

                val equipamiento = _recipe?.equipamientos ?: emptyList()

                val observaciones= observacionesInput.text.toString()

                val receta = _recipe

                if (receta != null) {
                    listener.onGenerarRecetaClick(receta)
                }
                else{
                    val ingredientes = "No hay recetas disponibles con esos ingredientes"

                    val equipamiento = "No hay recetas disponibles con esos equipamientos"

                    val observaciones= observacionesInput.text.toString()

                    val receta = receta

                    //listener.onGenerarRecetaClick(receta)
                }
            }
        }
    }

    private suspend fun fetchMeal(): RecipesItem {
        var recipe: RecipesItem
        var recipes: Recipes
        try {

            //crear una variable Ingredientes que será un string que contenga todos los valore de args.ingredientes separados por un ",+"
            val ingredientes = args.ingredientes.removeSuffix(".").split(", ")
            val ingredientesString = ingredientes.joinToString(",+")

            // Utiliza los ingredientes para buscar 1 recetaI
            recipes = getNetworkService().getMealByIngredients(ingredients = ingredientesString)

            // Generar un valor aleaorio entre 0 y el tamaño de la lista de recetas
            val random = Random.nextInt(0, recipes.size)

            // Obtener la receta de la lista de recetas
            recipe = recipes[random]


        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return recipe
    }

    private fun generarReceta( ingredientes: List<String>, equipamiento: List<String>, observaciones: String ): Receta {
        Log.w("ingredientes", ingredientes.toString())
        Log.w("equipamiento", equipamiento.toString())
        Log.w("observaciones", observaciones)

        return recetasPrueba.get(Random.nextInt(0, recetasPrueba.size))
    }
}