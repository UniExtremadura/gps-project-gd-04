package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.api.APICallback
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
        val ingredientesFiltrados = TreeSet<String>(todosLosIngredientes)

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

        if (_ingredients.isEmpty()) {
            fetchIngredients(object : APICallback {
                override fun onSuccessIngredient(ingredients: List<Ingredient?>) {
                    Log.d("IngredientFragment", "APICallback onCompleted")
                    val ingredients = ingredients.map { it?.toShowIngredients() }
                    // Actualizo la UI en el hilo principal
                    activity?.runOnUiThread {
                        _ingredients = ingredients?.filterNotNull() ?: emptyList()
                        todosIngredientesAdapter.updateData(_ingredients)
                        binding.spinner.visibility = View.GONE
                    }
                }


                override fun onError(cause : Throwable) {
                    Log.e("IngredientsFragment", "APICallback onError")
                    // Actualizo la UI en el hilo principal
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                        binding.spinner.visibility = View.GONE
                    }
                }

                override fun onCompletedMeal(Meal: List<Meal?>) {
                    TODO("Not yet implemented")
                }
            })
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

    private fun fetchIngredients(apiCallback: APICallback){
        BACKGROUND.submit {
            try {

                // Utiliza la letra aleatoria en la llamada a la API
                val result = getNetworkService().getIngredientsList().execute()

                if(result.isSuccessful){
                    apiCallback.onSuccessIngredient(result.body()!!.ingredients)
                } else {
                    apiCallback.onError(APIError("API Response Error: ${result.errorBody()}", null))
                }


            } catch (cause: Throwable) {
                // Actualizo la UI en el hilo principal si algo falla
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                    binding.spinner.visibility = View.GONE
                }
                Log.e("ListaFragment", "APICallback error")
                //Si algo lanza una excepción, informo al Caller
                throw APIError("Unable to fetch data from API", cause)
            }
        }
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
            itemSet = TreeSet<String>(todosLosIngredientes),
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