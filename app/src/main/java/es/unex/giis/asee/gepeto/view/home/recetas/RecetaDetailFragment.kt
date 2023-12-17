package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.databinding.FragmentRecetaDetailBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.view.home.HomeViewModel


class RecetaDetailFragment : Fragment() {

    private val args: RecetaDetailFragmentArgs by navArgs()

    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: RecetaDetailViewModel by viewModels {
        RecetaDetailViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecetaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getHeartIcon(favorita: Boolean): Int {
        return if (favorita) R.drawable.filled_heart else R.drawable.empty_heart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        viewModel.receta = args.receta
    }

    private fun setObservers() {

        // Observe the user in session
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        viewModel.recetaDetail.observe(viewLifecycleOwner) { receta ->
            receta?.let {
                recetaBinding(receta)
            }
        }

        viewModel.descriptionText.observe(viewLifecycleOwner) { text ->
            text?.let {
                binding.recetaDetalleDescripcion.text = it
            }
        }

        viewModel.equipmentText.observe(viewLifecycleOwner) { text ->
            text?.let {
                binding.recetaDetalleEquipamientos.text = it
            }
        }
    }

    private fun recetaBinding(receta: Receta) {

        binding.recetaDetalleNombre.text = receta.nombre

        binding.recetaDetalleIngredientes.text = receta.listaIngredientesDetalles()

        val imageUrl = receta.imagenPath

        Glide.with(requireContext()) // Usa el contexto de tu fragmento o actividad
            .load(imageUrl)
            .placeholder(R.drawable.ejemplo_plato)
            .into(binding.recetaDetalleImagen)

        binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))

        binding.recetaDetalleFavorita.setOnClickListener {
            // Lógica que se ejecuta cuando se hace clic en fav
            if (receta.favorita)
                viewModel.setNoFavorite(receta)
            else
                viewModel.setFavorite(receta)

            binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))
        }
    }
}