package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.TodoAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentListaBinding
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import kotlinx.coroutines.flow.emptyFlow
import java.util.TreeSet

class ListaFragment : Fragment() {

    private lateinit var _binding: FragmentListaBinding
    private val binding get() = _binding

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var ingredientesAdapter: ItemSwapAdapter

    private fun getSessionIngredients() : TreeSet<String> {
        val todoList = Session.getValue("todoList") as HashMap<String, Boolean>? ?: hashMapOf()

        val ingedientesSet = TreeSet(todosLosIngredientes)

        if (todoList.isEmpty()) {
            return ingedientesSet
        }

        for ((key, value) in todoList) {
            ingedientesSet.remove(key)
        }

        return ingedientesSet
    }

    private val todoList : HashMap<String, Boolean> =
        Session.getValue("todoList") as HashMap<String, Boolean>? ?: hashMapOf()

    //crea un treeSet vacio
    private var ingredientesSet : TreeSet<String> = TreeSet()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return _binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAllRecyclerView()

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)
    }



    private fun setUpAllRecyclerView() {
        ingredientesAdapter = ItemSwapAdapter(
            itemSet = ingredientesSet,
            onClick = {

            }
        )

        with(binding.rvTodosIngredientes) {
            adapter = ingredientesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
