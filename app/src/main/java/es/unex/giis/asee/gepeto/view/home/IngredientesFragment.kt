package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentIngredientesBinding
import es.unex.giis.asee.gepeto.utils.filtrarSwapItemElements
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import java.util.TreeSet


class IngredientesFragment : Fragment() {

    private lateinit var _binding: FragmentIngredientesBinding
    private val binding get() = _binding

    private lateinit var todosIngredientesAdapter: ItemSwapAdapter
    private lateinit var ingredientesSeleccionadosAdapter: ItemSwapAdapter

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: IngredientesViewModel by viewModels {
        IngredientesViewModel.Factory
    }

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

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()
        setUpButtonListener()

        viewModel.todosIngredientes = todosIngredientesAdapter
        viewModel.seleccionados = ingredientesSeleccionadosAdapter

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)

        filtrarSwapItemElements(
            binding.buscadorDeIngredientes,
            viewModel,
            todosIngredientesAdapter
        )
    }

    private fun setUpButtonListener() {
        with(binding) {
            btnCrearReceta.setOnClickListener() {
                val ingredientes = ingredientesSeleccionadosAdapter.getSet()

                if (ingredientes.isEmpty()) {
                    advertenciaLabel.visibility = View.VISIBLE
                } else {
                    homeViewModel.onEnviarIngredientesClick(ingredientes)
                }
            }
        }
    }

    private fun setUpAllRecyclerView () {
        todosIngredientesAdapter = ItemSwapAdapter(
            itemSet = TreeSet(todosLosIngredientes),
            onClick = {
                binding.advertenciaLabel.visibility = View.GONE
                viewModel.onClickTodos(it)
            })

        with(binding.rvTodosIngredientes) {
            adapter = todosIngredientesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSelectedRecyclerView () {
        ingredientesSeleccionadosAdapter = ItemSwapAdapter(
            itemSet = homeViewModel.ingredientesSeleccionados,
            onClick = {
                viewModel.onClickSeleccionados(it)
            })

        with(binding.rvIngredientesSeleccionados) {
            adapter = ingredientesSeleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}