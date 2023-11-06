package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.HistorialAdapter
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.data.toShowRecipe
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta
import kotlinx.coroutines.launch


class HistorialFragment : Fragment() {

    private var _recipes: List<Receta> = emptyList()

    private lateinit var listener: OnRecetaClickListener
    interface OnRecetaClickListener {
        fun onRecetaClick(receta: Receta)
    }

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistorialAdapter

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnRecetaClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        if (_recipes.isEmpty()) {

            binding.spinner.visibility = View.VISIBLE

            lifecycleScope.launch {
                if (_recipes.isEmpty()){
                    binding.spinner.visibility = View.VISIBLE

                    try {
                        _recipes = fetchMeals().filterNotNull().map { it.toShowRecipe() }
                        adapter.updateData(_recipes)
                    } catch (e: APIError) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    } finally {
                        binding.spinner.visibility = View.GONE
                    }
                }
            }
        }
    }

    private suspend fun fetchMeals(): Recipes{
        var recipes: Recipes
        try {
            // Genera una letra aleatoria en minúsculas

            // Utiliza la letra aleatoria en la llamada a la API
            recipes = getNetworkService().getMealByIngredients(ingredients = "flour,+sugar")

        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return recipes
    }

    private fun setUpRecyclerView() {
        adapter = HistorialAdapter(recetas = _recipes, onClick = {
            listener.onRecetaClick(it)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.nombre, Toast.LENGTH_SHORT).show()
            }
            , context = context
        )
        with(binding) {
            rvHistorialList.layoutManager = LinearLayoutManager(context)
            rvHistorialList.adapter = adapter
        }
        android.util.Log.d("RecetasFragment", "setUpRecyclerView")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}