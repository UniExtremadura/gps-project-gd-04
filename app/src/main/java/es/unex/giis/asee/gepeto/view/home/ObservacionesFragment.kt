package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.api.APICallback
import es.unex.giis.asee.gepeto.api.APIError
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.recetasPrueba
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.utils.BACKGROUND
import java.lang.RuntimeException
import kotlin.random.Random
import java.util.StringJoiner
import java.util.TreeSet


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()

    private lateinit var _binding: FragmentObservacionesBinding
    private val binding get() = _binding
    private lateinit var listener: OnGenerarRecetaListener

    interface OnGenerarRecetaListener {
        fun onGenerarRecetaClick( receta: Receta )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentObservacionesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if ( context is OnGenerarRecetaListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnGenerarRecetaListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val equipamientoSet = Session.getValue("equipamientosSeleccionados") as? TreeSet<String> ?: TreeSet()

        with (binding) {
            ingredientes.text = args.ingredientes
            equipamiento.text = equipamientoSet.joinToString(", ", "", ".")

            crearRecetaBtn.setOnClickListener {
                val ingredientes = args.ingredientes.removeSuffix(".").split(", ")
                val equipamiento = equipamientoSet.toList()
                val observaciones= observacionesInput.text.toString()
                val receta = generarReceta(ingredientes, equipamiento, observaciones)

                listener.onGenerarRecetaClick(receta)
            }
        }
    }

    private fun generarReceta( ingredientes: List<String>, equipamiento: List<String>, observaciones: String ): Receta {
        Log.w("ingredientes", ingredientes.toString())
        Log.w("equipamiento", equipamiento.toString())
        Log.w("observaciones", observaciones)

        return recetasPrueba.get(Random.nextInt(0, recetasPrueba.size))
    }
}