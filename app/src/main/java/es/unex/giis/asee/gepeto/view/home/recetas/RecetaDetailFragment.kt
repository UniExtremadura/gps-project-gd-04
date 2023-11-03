package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.databinding.FragmentRecetaDetailBinding
import es.unex.giis.asee.gepeto.databinding.FragmentRecetasBinding


class RecetaDetailFragment : Fragment() {


    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecetaDetailFragmentArgs by navArgs()

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
        binding.recetaDetalleDescripcion.text = receta.descripcion
        binding.recetaDetalleEquipamientos.text = receta.listaEquipamiento()
        binding.recetaDetalleIngredientes.text = receta.listaIngredientes()
        binding.recetaDetalleImagen.setImageResource(receta.imagen)

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
}