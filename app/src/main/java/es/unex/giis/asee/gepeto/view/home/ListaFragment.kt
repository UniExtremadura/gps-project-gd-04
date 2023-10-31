package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.TodoAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentListaBinding
import es.unex.giis.asee.gepeto.utils.Tuple
import java.util.TreeSet

/**
 * A simple [Fragment] subclass.
 * Use the [ListaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaFragment : Fragment() {

    private lateinit var _binding: FragmentListaBinding
    private val binding get() = _binding

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var ingredientesAdapter: ItemSwapAdapter

    private fun getSessionTodo (): MutableList<Tuple<String, Boolean>> {
        if (Session.getValue("todoList") == null) {
            return mutableListOf<Tuple<String, Boolean>>()
        }

        return Session.getValue("todoList") as MutableList<Tuple<String, Boolean>>
    }

    private fun getSessionIngredients() : TreeSet<String> {
        val ingedientesSet = TreeSet<String>(todosLosIngredientes)

        if (getSessionTodo().isEmpty()) {
            return ingedientesSet
        }

        for (item in getSessionTodo()) {
            ingedientesSet.remove(item.first)
        }

        return ingedientesSet
    }

    private val todoList : MutableList<Tuple<String, Boolean>> = getSessionTodo()

    private var ingredientesSet: TreeSet<String> = getSessionIngredients()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val filtro = binding.buscadorDeIngredientes

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        filtro.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                val listaFiltrada = ingredientesSet.filter {
                    it.contains(text, ignoreCase = true)
                }
                ingredientesAdapter.swap(TreeSet(listaFiltrada))
            }

            override fun afterTextChanged(s: Editable?) {

            }

        } )

    }

    private fun setUpSelectedRecyclerView() {
        todoAdapter = TodoAdapter(
            todoList = todoList,
            onClick = {
                todoAdapter.check(it)
            },
            onLongClick = {
                todoAdapter.remove(it)
                ingredientesSet.add(it)
                ingredientesAdapter.add(it)
            }
        )

        with(binding.rvIngredientesSeleccionados) {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context)
//            setHasFixedSize(true)
        }
    }

    private fun setUpAllRecyclerView() {
        ingredientesAdapter = ItemSwapAdapter(
            itemSet = ingredientesSet,
            onClick = {
                todoAdapter.add(it)
                ingredientesAdapter.remove(it)
                ingredientesSet.remove(it)
            }
        )

        with(binding.rvTodosIngredientes) {
            adapter = ingredientesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
