package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.adapters.TodoAdapter
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentListaBinding
import es.unex.giis.asee.gepeto.utils.filtrarSwapItemElements
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import java.util.TreeSet

class ListaFragment : Fragment() {

    private lateinit var _binding: FragmentListaBinding
    private val binding get() = _binding

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var ingredientesAdapter: ItemSwapAdapter

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: ListaViewModel by viewModels {
        ListaViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        viewModel.seleccionados = todoAdapter
        viewModel.todosIngredientes = ingredientesAdapter

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)

        filtrarSwapItemElements(
            binding.buscadorDeIngredientes,
            viewModel,
            ingredientesAdapter
        )
    }

    private fun setUpSelectedRecyclerView() {
        todoAdapter = TodoAdapter(
            todoMap = homeViewModel.todoSeleccionados,
            onClick = {
                viewModel.onClickSeleccionados(it)
            },
            onLongClick = {
                viewModel.onLongClickSeleccionados(it)
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
            itemSet = TreeSet(todosLosIngredientes),
            onClick = {
                viewModel.onClickTodos(it)
            }
        )

        with(binding.rvTodosIngredientes) {
            adapter = ingredientesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
