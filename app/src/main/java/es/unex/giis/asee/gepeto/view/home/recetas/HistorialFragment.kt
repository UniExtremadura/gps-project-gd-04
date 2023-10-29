package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.HistorialAdapter
import es.unex.giis.asee.gepeto.data.recetasPrueba
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistorialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistorialFragment : Fragment() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun setUpRecyclerView() {
        adapter = HistorialAdapter(recetas = recetasPrueba, onClick = {
            listener.onRecetaClick(it)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.nombre, Toast.LENGTH_SHORT).show()
            }
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