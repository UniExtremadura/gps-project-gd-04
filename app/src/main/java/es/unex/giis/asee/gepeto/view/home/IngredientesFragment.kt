package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.adapters.IngredientesAdapter
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentIngredientesBinding
import java.util.TreeSet

/**
 * A simple [Fragment] subclass.
 * Use the [IngredientesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngredientesFragment : Fragment() {

    private lateinit var _binding: FragmentIngredientesBinding

    private lateinit var todosIngredientesAdapter: IngredientesAdapter
    private lateinit var ingredientesSeleccionadosAdapter: IngredientesAdapter

    private val binding get() = _binding

    private lateinit var filtroIngredientes : EditText

    // Esta lista almacenará todos los cambios que se hagan en la lista de todos los ingredientes
    private var listaIngredientes : TreeSet<String> = TreeSet<String>(todosLosIngredientes)
    // Utilizo un treeset porque no admite duplicados y los elementos están ordenados automaticamente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtroIngredientes = binding.buscadorDeIngredientes.findViewById(R.id.buscador_de_ingredientes)

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        filtroIngredientes.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                val listaFiltrada = listaIngredientes.filter {
                    it.contains(text, ignoreCase = true)
                }
                todosIngredientesAdapter.swap(TreeSet<String>(listaFiltrada))
            }

            override fun afterTextChanged(s: Editable?) {

            }

        } )
    }

    fun setUpAllRecyclerView () {
        todosIngredientesAdapter = IngredientesAdapter(
            ingredientes_list = TreeSet<String>(todosLosIngredientes),
            onClick = {

            ingredientesSeleccionadosAdapter.add(it)
            todosIngredientesAdapter.remove(it)
            listaIngredientes.remove(it)

        })

        with(binding) {
            rvTodosIngredientes.adapter = todosIngredientesAdapter
            rvTodosIngredientes.layoutManager = LinearLayoutManager(context)
        }
    }

    fun setUpSelectedRecyclerView () {
        ingredientesSeleccionadosAdapter = IngredientesAdapter(
            ingredientes_list = TreeSet<String>(),
            onClick = {

            todosIngredientesAdapter.add(it)
            ingredientesSeleccionadosAdapter.remove(it)
            listaIngredientes.add(it)

        })

        with(binding) {
            rvIngredientesSeleccionados.adapter = ingredientesSeleccionadosAdapter
            rvIngredientesSeleccionados.layoutManager = LinearLayoutManager(context)
        }
    }
}