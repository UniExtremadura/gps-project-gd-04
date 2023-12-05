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
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.toEquipamiento
import es.unex.giis.asee.gepeto.data.toRecipe
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.databinding.FragmentRecetaDetailBinding
import es.unex.giis.asee.gepeto.model.Equipamiento
import es.unex.giis.asee.gepeto.model.Pasos
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.launch


class RecetaDetailFragment : Fragment() {

    private val args: RecetaDetailFragmentArgs by navArgs()

    private var _binding: FragmentRecetaDetailBinding? = null
    private val binding get() = _binding!!

    private var steps: List<Pasos> = emptyList()
    private var equipments: List<Equipamiento> = emptyList()

    private lateinit var repository: Repository

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

        val appContainer = (this.activity?.application as GepetoApplication).appContainer
        repository = appContainer.repository

        val receta = args.receta

        binding.recetaDetalleNombre.text = receta.nombre

        lifecycleScope.launch {
            if (steps.isEmpty()){

                try {
                    steps = fetchPasos().filterNotNull().map { it.toRecipe() }

                    val descripcionText = steps.flatMap { it.descripcion }.joinToString("\n\n - ", prefix = "Pasos:\n\n - ")

                    binding.recetaDetalleDescripcion.text = descripcionText

                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            if (equipments.isEmpty()){

                try {
                    equipments = listOf(fetchEquipamiento().toEquipamiento())

                    println(equipments)

                    val equipamientoText = equipments.flatMap { it.descripcion.map { desc -> desc.trim().capitalize() } }
                        .joinToString("\n\n - ", prefix = "Equipamiento:\n\n - ")

                    binding.recetaDetalleEquipamientos.text = equipamientoText

                } catch (e: APIError) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Espero 1s
        Thread.sleep(1000)

        binding.recetaDetalleIngredientes.text = receta.listaIngredientesDetalles()

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
                //db.recetaDao().update(receta)
                repository.recipeToLibrary(receta, (Session.getValue("user") as User).userId!!)
            }
            binding.recetaDetalleFavorita.setImageResource(getHeartIcon(receta.favorita))
        }
    }

    private suspend fun fetchPasos(): Instructions {
        var pasos: Instructions
        val receta = args.receta
        try {
            // Utiliza la letra aleatoria en la llamada a la API
            //pasos = getNetworkService().getMealSteps(receta.recetaId.toString())
            pasos = repository.getMealSteps(receta.recetaId.toString())

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
            //equipamiento = getNetworkService().getMealEquipments(receta.recetaId.toString())
            equipamiento = repository.getMealEquipments(receta.recetaId.toString())

            if (equipamiento.equipment.isEmpty()){
                throw APIError("No hay equipamiento para esta receta", null)
            }

            return equipamiento
        } catch (cause: Throwable) {
            throw APIError("Error al obtener los datos del equipamiento", cause)
        }
    }
}