package es.unex.giis.asee.gepeto.view.home.recetas

import android.content.Context
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
import es.unex.giis.asee.gepeto.databinding.FragmentRecetaDetailBinding
import es.unex.giis.asee.gepeto.model.Equipamiento
import es.unex.giis.asee.gepeto.model.Pasos
import kotlinx.coroutines.launch


class RecetaDetailFragment : Fragment() {


    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecetaDetailFragmentArgs by navArgs()

    private var _steps: List<Pasos> = emptyList()

    private var _equipments: List<Equipamiento> = emptyList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

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


            }
        }

        lifecycleScope.launch {
            if (_equipments.isEmpty()){


            }
        }

        //Espero 1s
        Thread.sleep(1000)

        //binding.recetaDetalleDescripcion.text = receta.showDescripcion()

        //binding.recetaDetalleEquipamientos.text = receta.listaEquipamiento()

        binding.recetaDetalleIngredientes.text = receta.listaIngredientesDetalles()


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
            lifecycleScope.launch {
            }
            binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))
        }
    }
}