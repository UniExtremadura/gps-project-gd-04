package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.utils.filtrarRecetasFilter
import es.unex.giis.asee.gepeto.view.home.HomeViewModel


class HistorialFragment : Fragment() {

    private lateinit var adapter: RecetasAdapter

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: HistorialViewModel by viewModels {
        HistorialViewModel.Factory
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.adapter = adapter

        setObservers()

        filtrarRecetasFilter(
            binding.buscadorDeRecetas,
            viewModel,
            adapter
        )
    }

    private fun setObservers() {

        // Observe the user in session
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.refresh()
        }

        // show the spinner when [spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { receta ->
            binding.spinner.visibility = if (receta) View.VISIBLE else View.GONE
        }

        // show the buscador when [buscador] is true
        viewModel.buscador.observe(viewLifecycleOwner) { receta ->
            binding.buscadorRecetaContainer.visibility = if (receta) View.VISIBLE else View.GONE
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
    }

    private fun setUpRecyclerView() {
        adapter = RecetasAdapter(recetas = emptyList(),
            onClick = {
                homeViewModel.onRecetaClick(it)
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