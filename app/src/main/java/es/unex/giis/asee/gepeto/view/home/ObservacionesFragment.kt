package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.TreeSet


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()
    private lateinit var repository: Repository
    private lateinit var listener: OnGenerarRecetaListener

    private lateinit var _binding: FragmentObservacionesBinding
    private val binding get() = _binding

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

        val appContainer = (this.activity?.application as GepetoApplication).appContainer
        repository = appContainer.repository

        if ( context is OnGenerarRecetaListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnGenerarRecetaListener")
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
                        val recipe = repository.fetchRecentRecipe(listaIngredientes)
                        val user = Session.getValue("user") as User

                        //db.recetaDao().insertAndRelate(_recipe!!, user.userId!!)
                        repository.insertAndRelate(recipe, user.userId!!)

                        listener.onGenerarRecetaClick(recipe)
                    } catch (e: APIError) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}