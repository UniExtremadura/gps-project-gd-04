package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.api.Recipes
import es.unex.giis.asee.gepeto.data.api.RecipesItem
import es.unex.giis.asee.gepeto.data.toRecipe
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import kotlin.random.Random
import java.util.TreeSet


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()

    private lateinit var _binding: FragmentObservacionesBinding

    private var _recipe : Receta? = null
    private lateinit var db: GepetoDatabase

    private lateinit var repository: Repository

    private val binding get() = _binding
    private lateinit var listener: OnGenerarRecetaListener

    interface OnGenerarRecetaListener {
        fun onGenerarRecetaClick( receta: Receta )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentObservacionesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = GepetoDatabase.getInstance(context)!!
        repository = Repository.getInstance(db.recetaDao(), getNetworkService())
        if ( context is OnGenerarRecetaListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnGenerarRecetaListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suspend { repository.tryUpdateRecentRecipesCache() }
        val equipamientoSet = Session.getValue("equipamientosSeleccionados") as? TreeSet<*> ?: TreeSet<String>()

        with (binding) {
            ingredientes.text = args.ingredientes
            val listaIngredientes = args.ingredientes.removeSuffix(".").split(", ")

            equipamiento.text = equipamientoSet.joinToString(separator = ", ", postfix = "."  )

            crearRecetaBtn.setOnClickListener {

                lifecycleScope.launch {
                    try {
                        _recipe = repository.fetchRecentRecipe(listaIngredientes)
                        val user = Session.getValue("user") as User

                        //db.recetaDao().insertAndRelate(_recipe!!, user.userId!!)
                        repository.insertAndRelate(_recipe!!, user.userId!!)

                        listener.onGenerarRecetaClick(_recipe!!)
                    } catch (e: APIError) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}