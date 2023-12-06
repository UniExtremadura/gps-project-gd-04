package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.utils.filtrarRecetas
import es.unex.giis.asee.gepeto.utils.filtrarRecetasViewModel
import kotlinx.coroutines.launch


class HistorialFragment : Fragment() {

    var recetas: List<Receta> = emptyList()

    private lateinit var repository: Repository
    private lateinit var listener: OnRecetaClickListener
    interface OnRecetaClickListener {
        fun onRecetaClick(receta: Receta)
    }

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecetasAdapter

    private val viewModel: HistorialViewModel by viewModels {
        HistorialViewModel.Factory
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is OnRecetaClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.adapter = adapter

        val appContainer = (this.activity?.application as GepetoApplication).appContainer
        repository = appContainer.repository

        // show the spinner when [spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { receta ->
            binding.spinner.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // show the buscador when [buscador] is true
        viewModel.buscador.observe(viewLifecycleOwner) { receta ->
            binding.buscadorDeRecetas.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // show the noRecetasMessage when [noRecetasMessage] is true
        viewModel.noHayRecetasMessage.observe(viewLifecycleOwner) { receta ->
            binding.noHayRecetas.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // Show a Toast whenever the [toast] is updated a non-null value
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        filtrarRecetasViewModel(
            binding.buscadorDeRecetas,
            viewModel,
            adapter
        )
    }

    private fun setUpRecyclerView() {
        adapter = RecetasAdapter(recetas = emptyList(),
            onClick = {
                listener.onRecetaClick(it)
            },
            onLongClick = {
                viewModel.setRecetaFav(it)
            }
            , context = context
        )
        with(binding) {
            rvHistorialList.layoutManager = LinearLayoutManager(context)
            rvHistorialList.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}