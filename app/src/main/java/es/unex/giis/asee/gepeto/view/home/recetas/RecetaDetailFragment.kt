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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecetaDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecetaDetailFragment : Fragment() {


    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecetaDetailFragmentArgs by navArgs()


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecetaDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecetaDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}