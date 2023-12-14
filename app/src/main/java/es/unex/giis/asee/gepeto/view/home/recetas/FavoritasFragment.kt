package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.databinding.FragmentFavoritasBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.utils.filtrarRecetasFilter


class FavoritasFragment : Fragment() {

    private var recetasFav: List<Receta> = emptyList()

    private lateinit var repository: Repository
    private lateinit var listener: OnReceta2ClickListener
    interface OnReceta2ClickListener {
        fun onReceta2Click(receta: Receta)
    }

    private var _binding: FragmentFavoritasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecetasAdapter

    private val viewModel: FavoritasViewModel by viewModels {
        FavoritasViewModel.Factory
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is OnReceta2ClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.adapter = adapter

        val appContainer = (this.activity?.application as GepetoApplication).appContainer
        repository = appContainer.repository

        setObservers()

        filtrarRecetasFilter(
            binding.buscadorDeFavoritas,
            viewModel,
            adapter
        )
    }

    private fun setUpRecyclerView() {
        adapter = RecetasAdapter(recetas = emptyList(),
            onClick = {
                listener.onReceta2Click(it)
            },
            onLongClick = {
                viewModel.unFavReceta(it)
                viewModel.refresh()
            },
            context = context
        )
        with(binding) {
            rvFavoritasList.layoutManager = LinearLayoutManager(context)
            rvFavoritasList.adapter = adapter
        }
    }

    private fun setObservers() {
        // show the spinner when [spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { receta ->
            binding.spinner.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // show the buscador when [buscador] is true
        viewModel.buscador.observe(viewLifecycleOwner) { receta ->
            binding.buscadorFavoritasContainer.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // show the noRecetasMessage when [noRecetasMessage] is true
        viewModel.noHayFavoritasMessage.observe(viewLifecycleOwner) { receta ->
            binding.noHayFavoritas.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // Show a Toast whenever the [toast] is updated a non-null value
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}