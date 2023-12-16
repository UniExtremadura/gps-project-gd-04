package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()

    private lateinit var _binding: FragmentObservacionesBinding
    private val binding get() = _binding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: ObservacionesViewModel by viewModels {
        ObservacionesViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObservacionesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        viewModel.user = homeViewModel.userInSession
        viewModel.ingredientesText = args.ingredientes
        viewModel.equipamientoList = homeViewModel.equipamientoSeleccionado
        viewModel.setAttributes()

        with (binding) {
            ingredientes.text = viewModel.ingredientesText

            crearRecetaBtn.setOnClickListener {
                viewModel.generarReceta()
            }
        }
    }

    private fun setObservers() {
        viewModel.equipamientoText.observe(viewLifecycleOwner) { equipamientoText ->
            equipamientoText?.let {
                binding.equipamiento.text = equipamientoText
            }
        }

        viewModel.receta.observe(viewLifecycleOwner) { receta ->
            receta?.let {
                viewModel.vincularRecetaConUsuario(receta)
                homeViewModel.onGenerarRecetaClick(receta)
                viewModel.onRecetaSent()
            }
        }

        // Show a Toast whenever the [toast] is updated a non-null value
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
    }
}