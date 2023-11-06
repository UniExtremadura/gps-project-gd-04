package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.toShowRecipe
import es.unex.giis.asee.gepeto.databinding.FragmentRecetaDetailBinding
import es.unex.giis.asee.gepeto.model.Pasos
import kotlinx.coroutines.launch


class RecetaDetailFragment : Fragment() {


    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecetaDetailFragmentArgs by navArgs()

    private var _steps: List<Pasos> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecetaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getHeartIcon(favorita: Boolean): Int {
        return if (favorita) R.drawable.filled_heart else R.drawable.empty_heart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receta = args.receta
        binding.recetaDetalleNombre.text = receta.nombre

        lifecycleScope.launch {
            if (_steps.isEmpty()){

                try {
                    _steps = fetchPasos().filterNotNull().map { it.toShowRecipe() }

                    val descripcionText = _steps.flatMap { it.descripcion }.joinToString("\n", prefix = "Pasos:\n")

                    binding.recetaDetalleDescripcion.text = descripcionText

                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //binding.recetaDetalleDescripcion.text = receta.showDescripcion()

        binding.recetaDetalleEquipamientos.text = receta.listaEquipamiento()

        binding.recetaDetalleIngredientes.text = receta.listaIngredientes()


        //binding.recetaDetalleImagen.setImageResource(receta.imagen)
        val imageUrl = receta.imagenPath

        Glide.with(requireContext()) // Usa el contexto de tu fragmento o actividad
            .load(imageUrl)
            .placeholder(R.drawable.ejemplo_plato)
            .into(binding.recetaDetalleImagen)

        binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))

        binding.recetaDetalleFavorita.setOnClickListener {
            // Lógica que se ejecuta cuando se hace clic en fav
            receta.favorita = !receta.favorita
            binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))
        }


        //binding.tvDescription.text = show.description
        //binding.tvYear.text = show.year
        //binding.swFav.isChecked = show.isFavorite
        //binding.coverImg.setImageResource(show.image)
        //binding.bannerImg.setImageResource(show.banner)
    }

    private suspend fun fetchPasos(): Instructions {
        var pasos: Instructions
        val receta = args.receta
        try {
            // Utiliza la letra aleatoria en la llamada a la API
            pasos = getNetworkService().getMealSteps(receta.idReceta)

        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return pasos
    }
}