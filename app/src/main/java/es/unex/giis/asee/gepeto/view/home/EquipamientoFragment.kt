package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.ItemSwapAdapter
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import es.unex.giis.asee.gepeto.databinding.FragmentEquipamientoBinding
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

    private val equipamientosSet : TreeSet<String> = TreeSet<String>(equipamientosDeCocina)

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

        val filtro = binding.buscadorDeEquipamientos

        setUpAllRecyclerView()
        setUpSelectedRecyclerView()

        filtro.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                val listaFiltrada = equipamientosSet.filter {
                    it.contains(text, ignoreCase = true)
                }
                equipamientosAdapter.swap(TreeSet<String>(listaFiltrada))
            }

            override fun afterTextChanged(s: Editable?) {

            }

        } )

    }

    private fun setUpAllRecyclerView () {
        equipamientosAdapter = ItemSwapAdapter(
            itemSet = TreeSet(equipamientosSet),
            onClick = {

                seleccionadosAdapter.add(it)
                equipamientosAdapter.remove(it)
                equipamientosSet.remove(it)

            })

        with(binding.rvTodosEquipamientos) {
            adapter = equipamientosAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSelectedRecyclerView () {
        seleccionadosAdapter = ItemSwapAdapter(
            itemSet = TreeSet<String>(),
            onClick = {

                equipamientosAdapter.add(it)
                seleccionadosAdapter.remove(it)
                equipamientosSet.add(it)

            })

        with(binding.rvEquipamientosSeleccionados) {
            adapter = seleccionadosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}