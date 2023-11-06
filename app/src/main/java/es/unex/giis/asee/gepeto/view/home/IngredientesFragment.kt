package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.data.toShowIngredients
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentIngredientesBinding
import es.unex.giis.asee.gepeto.model.Ingrediente
import es.unex.giis.asee.gepeto.utils.BACKGROUND
import es.unex.giis.asee.gepeto.utils.filtrarLista
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.TreeSet

/**
 * A simple [Fragment] subclass.
 * Use the [IngredientesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngredientesFragment : Fragment() {

    private lateinit var _binding: FragmentIngredientesBinding
    private val binding get() = _binding

    private var _ingredients: List<Ingrediente> = emptyList()

    private lateinit var todosIngredientesAdapter: ItemSwapAdapter
    private lateinit var ingredientesSeleccionadosAdapter: ItemSwapAdapter

    private lateinit var listener: OnCrearRecetaListener

    interface OnCrearRecetaListener {
        fun onCrearRecetaClick(ingredientes: TreeSet<String> )
    }

    private fun getIngredientes () : TreeSet<String> {
        val ingredientes = Session.getValue("ingredientesSeleccionados") as TreeSet<*>? ?: TreeSet<String>()
        val ingredientesFiltrados = TreeSet<String>()

        if (ingredientes.isEmpty()) {
            return ingredientesFiltrados
        }

        for ( item in ingredientes ) {
            ingredientesFiltrados.remove(item)
        }

        return ingredientesFiltrados
    }

    // Esta lista almacenará todos los cambios que se hagan en la lista de todos los ingredientes
    private var listaIngredientes : TreeSet<String> = getIngredientes()
    // Utilizo un treeset porque no admite duplicados y los elementos están ordenados automaticamente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if ( context is OnCrearRecetaListener ) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCrearRecetaListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            if(_ingredients.isEmpty()){
                binding.spinner.visibility = View.VISIBLE

                try {
                    _ingredients = fetchIngredients().filterNotNull().map { it.toShowIngredients() }
                    todosIngredientesAdapter.updateData(_ingredients)

                    //Añadimos los ingredientes a la lista de todos los ingredientes
                    listaIngredientes.addAll(_ingredients.map { it.nombre })
                    if(todosIngredientesAdapter != null){
                        todosIngredientesAdapter.swap(listaIngredientes)
                    }
                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.spinner.visibility = View.GONE
                }
            }
        }

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()
        setUpButtonListener()

        filtrarLista(
            binding.buscadorDeIngredientes,
            listaIngredientes,
            todosIngredientesAdapter
        )
    }

    private suspend fun fetchIngredients(): List<Ingredient>{
        var ingredients = listOf<Ingredient>()
        try {
            ingredients = getNetworkService().getIngredientsList().ingredients
        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return ingredients
    }

    private fun setUpButtonListener() {
        with(binding) {
            btnCrearReceta.setOnClickListener() {

                val ingredientes = ingredientesSeleccionadosAdapter.getSet()

                if (ingredientes.isEmpty()) {
                    advertenciaLabel.visibility = View.VISIBLE
                } else {
                    listener.onCrearRecetaClick(ingredientesSeleccionadosAdapter.getSet())
                }
            }
        }
    }

    private fun setUpAllRecyclerView () {
        todosIngredientesAdapter = ItemSwapAdapter(

            itemSet = TreeSet<String>(listaIngredientes),

            onClick = {

                with(binding.advertenciaLabel) {
                    if (visibility == View.VISIBLE) {
                        visibility = View.GONE
                    }
                }

                ingredientesSeleccionadosAdapter.add(it)
                todosIngredientesAdapter.remove(it)
                listaIngredientes.remove(it)

                Session.setValue("ingredientesSeleccionados", ingredientesSeleccionadosAdapter.getSet())
            })

        with(binding.rvTodosIngredientes) {
            adapter = todosIngredientesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSelectedRecyclerView () {
        ingredientesSeleccionadosAdapter = ItemSwapAdapter(
            itemSet = Session.getValue("ingredientesSeleccionados") as TreeSet<String>? ?: TreeSet<String>(),
            onClick = {

                todosIngredientesAdapter.add(it)
                ingredientesSeleccionadosAdapter.remove(it)
                listaIngredientes.add(it)

                Session.setValue("ingredientesSeleccionados", ingredientesSeleccionadosAdapter.getSet())
            })

        with(binding.rvIngredientesSeleccionados) {
            adapter = ingredientesSeleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}