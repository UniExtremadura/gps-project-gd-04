package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentIngredientesBinding
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
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

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()
        setUpButtonListener()

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)

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

                Session.setValue("ingredientesSeleccionados", ingredientesSeleccionadosAdapter.getSet())
            })

        with(binding.rvIngredientesSeleccionados) {
            adapter = ingredientesSeleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}