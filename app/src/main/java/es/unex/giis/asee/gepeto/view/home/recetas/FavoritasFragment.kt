package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.databinding.FragmentFavoritasBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.view.home.HomeActivity
import kotlinx.coroutines.launch


class FavoritasFragment : Fragment() {
    private lateinit var listener: OnReceta2ClickListener
    interface OnReceta2ClickListener {
        fun onReceta2Click(receta: Receta)
    }

    private lateinit var recetasFav: List<Receta>

    private var _binding: FragmentFavoritasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecetasAdapter

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnReceta2ClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.mostrarLupaAppbar(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as HomeActivity?)?.mostrarLupaAppbar(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter = RecetasAdapter(recetas = emptyList(),
            onClick = {
            },
            onLongClick = {
            },
            context = context
        )
        with(binding) {
            rvFavoritasList.layoutManager = LinearLayoutManager(context)
            rvFavoritasList.adapter = adapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}