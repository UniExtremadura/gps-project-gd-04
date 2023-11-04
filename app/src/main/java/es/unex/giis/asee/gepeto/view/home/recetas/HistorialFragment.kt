package es.unex.giis.asee.gepeto.view.home.recetas

import MealList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.HistorialAdapter
import es.unex.giis.asee.gepeto.api.APICallback
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.data.recetasPrueba
import es.unex.giis.asee.gepeto.data.toShowMeal
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.utils.BACKGROUND


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
            fetchMeals(object : APICallback {
                override fun onCompleted(meals: List<Meal?>) {
                    Log.d("HistorialFragment", "APICallback onCompleted")
                    val meals = meals.map { it?.toShowMeal() }
                    // Actualizo la UI en el hilo principal
                    activity?.runOnUiThread {
                        _meals = meals?.filterNotNull() ?: recetasPrueba
                        adapter.updateData(_meals)
                        binding.spinner.visibility = View.GONE
                    }
                }


                override fun onError(cause : Throwable) {
                    Log.e("HistorialFragment", "APICallback onError")
                    // Actualizo la UI en el hilo principal
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                        binding.spinner.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun fetchMeals(apiCallback: APICallback){
        BACKGROUND.submit {
            try {

                // Usar una llamada bloqueante para hacer una petición
                val result = getNetworkService().getListOfMealsByFirstLetter("b").execute()

                if(result.isSuccessful){
                    apiCallback.onCompleted(result.body()!!.meals)
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