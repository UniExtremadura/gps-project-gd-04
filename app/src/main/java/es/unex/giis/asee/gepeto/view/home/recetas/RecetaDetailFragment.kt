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
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.toEquipamiento
import es.unex.giis.asee.gepeto.data.toRecipe
import es.unex.giis.asee.gepeto.database.GepetoDatabase
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
    private lateinit var db: GepetoDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = GepetoDatabase.getInstance(context)!!
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

        val receta = args.receta

        binding.recetaDetalleNombre.text = receta.nombre

        lifecycleScope.launch {
            if (_steps.isEmpty()){

                try {
                    _steps = fetchPasos().filterNotNull().map { it.toRecipe() }

                    val descripcionText = _steps.flatMap { it.descripcion }.joinToString("\n\n - ", prefix = "Pasos:\n\n - ")

                    binding.recetaDetalleDescripcion.text = descripcionText

                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            if (_equipments.isEmpty()){

                try {
                    _equipments = listOf(fetchEquipamiento().toEquipamiento())

                    println(_equipments)

                    val equipamientoText = _equipments.flatMap { it.descripcion.map { desc -> desc.trim().capitalize() } }
                        .joinToString("\n\n - ", prefix = "Equipamiento:\n\n - ")

                    binding.recetaDetalleEquipamientos.text = equipamientoText

                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
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
                db.recetaDao().update(receta)
            }
            binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))
        }
    }

    private suspend fun fetchPasos(): Instructions {
        var pasos: Instructions
        val receta = args.receta
        try {
            // Utiliza la letra aleatoria en la llamada a la API
            pasos = getNetworkService().getMealSteps(receta.recetaId.toString())

            println(pasos)
            println(receta.recetaId.toString())
        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos", cause)
        }

        return pasos
    }

    private suspend fun fetchEquipamiento(): Equipments {
        val receta = args.receta
        val equipamiento : Equipments
        try {
            equipamiento = getNetworkService().getMealEquipments(receta.recetaId.toString())

            if (equipamiento.equipment.isEmpty()){
                throw APIError("No hay equipamiento para esta receta", null)
            }

            return equipamiento
        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos del equipamiento", cause)
        }
    }
}