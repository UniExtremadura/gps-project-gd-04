package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import android.util.Log
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
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.data.recetasPrueba
import es.unex.giis.asee.gepeto.data.toShowMeal
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.utils.BACKGROUND
import kotlinx.coroutines.launch
import kotlin.random.Random



class HistorialFragment : Fragment() {

    private var _meals: List<Receta> = emptyList()

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

        if (_meals.isEmpty()) {

            binding.spinner.visibility = View.VISIBLE

            lifecycleScope.launch {
                if (_meals.isEmpty()){
                    binding.spinner.visibility = View.VISIBLE

                    try {
                        _meals = fetchMeals().filterNotNull().map { it.toShowMeal() }
                        adapter.updateData(_meals)
                    } catch (e: APIError) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    } finally {
                        binding.spinner.visibility = View.GONE
                    }
                }
            }
        }
    }

    private suspend fun fetchMeals(): List<Meal>{
        var meals = listOf<Meal>()
        try {
            // Genera una letra aleatoria en minúsculas
            val letras = "abcdefghjklmnoprstvw"
            val letraAleatoria = letras[Random.nextInt(letras.length)]

            // Utiliza la letra aleatoria en la llamada a la API
            meals = getNetworkService().getListOfMealsByFirstLetter(letraAleatoria.toString()).meals

        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return meals
    }

    private fun setUpRecyclerView() {
        adapter = HistorialAdapter(recetas = _meals, onClick = {
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