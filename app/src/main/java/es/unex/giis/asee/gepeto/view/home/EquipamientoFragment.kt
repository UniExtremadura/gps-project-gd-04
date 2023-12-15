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
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import es.unex.giis.asee.gepeto.databinding.FragmentEquipamientoBinding
import es.unex.giis.asee.gepeto.utils.filtrarSwapItemElements
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import java.util.TreeSet


class EquipamientoFragment : Fragment() {

    private lateinit var _binding: FragmentEquipamientoBinding
    private val binding get() = _binding

    private lateinit var equipamientosAdapter: ItemSwapAdapter
    private lateinit var seleccionadosAdapter: ItemSwapAdapter

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: EquipamientoViewModel by viewModels {
        EquipamientoViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEquipamientoBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        viewModel.todosEquipamiento = equipamientosAdapter
        viewModel.seleccionados = seleccionadosAdapter

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)

        filtrarSwapItemElements(
            binding.buscadorDeEquipamientos,
            viewModel,
            equipamientosAdapter
        )
    }

    private fun setUpAllRecyclerView () {
        equipamientosAdapter = ItemSwapAdapter(
            itemSet = TreeSet(equipamientosDeCocina),
            onClick = {
                viewModel.onClickTodos(it)
            })

        with(binding.rvTodosEquipamientos) {
            adapter = equipamientosAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSelectedRecyclerView () {
        seleccionadosAdapter = ItemSwapAdapter(
            itemSet = homeViewModel.equipamientoSeleccionado,
            onClick = {
                viewModel.onClickSeleccionados(it)
            })

        with(binding.rvEquipamientosSeleccionados) {
            adapter = seleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}