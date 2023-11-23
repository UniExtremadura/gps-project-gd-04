package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import es.unex.giis.asee.gepeto.databinding.FragmentEquipamientoBinding
import es.unex.giis.asee.gepeto.utils.filtrarLista
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import java.util.TreeSet

/**
 * A simple [Fragment] subclass.
 * Use the [EquipamientoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipamientoFragment : Fragment() {

    private lateinit var _binding: FragmentEquipamientoBinding

    private lateinit var equipamientosAdapter: ItemSwapAdapter
    private lateinit var seleccionadosAdapter: ItemSwapAdapter

    private val binding get() = _binding

    private fun getEquipamientos () : TreeSet<String> {
        val equipamientos = Session.getValue("equipamientosSeleccionados") as TreeSet<*>? ?: TreeSet<String>()
        val equipamientosFiltrados = TreeSet<String>(equipamientosDeCocina)

        if (equipamientos.isEmpty()) {
            return equipamientosFiltrados
        }

        for ( item in equipamientos ) {
            equipamientosFiltrados.remove(item)
        }

        return equipamientosFiltrados
    }

    private val equipamientosSet : TreeSet<String> = getEquipamientos()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEquipamientoBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ocultarBottomNavigation(view, bottomNavigationView)

        filtrarLista(
            binding.buscadorDeEquipamientos,
            equipamientosSet,
            equipamientosAdapter
        )

    }

    private fun setUpAllRecyclerView () {
        equipamientosAdapter = ItemSwapAdapter(
            itemSet = equipamientosSet,
            onClick = {
                

                Session.setValue("equipamientosSeleccionados", seleccionadosAdapter.getSet())
            })

        with(binding.rvTodosEquipamientos) {
            adapter = equipamientosAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSelectedRecyclerView () {
        seleccionadosAdapter = ItemSwapAdapter(
            itemSet = Session.getValue("equipamientosSeleccionados") as TreeSet<String>? ?: TreeSet<String>(),
            onClick = {


                Session.setValue("equipamientosSeleccionados", seleccionadosAdapter.getSet())
            })

        with(binding.rvEquipamientosSeleccionados) {
            adapter = seleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}