package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.databinding.FragmentObservacionesBinding
import java.util.StringJoiner


class ObservacionesFragment : Fragment() {

    private val args: ObservacionesFragmentArgs by navArgs()

    private lateinit var _binding: FragmentObservacionesBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentObservacionesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.observacionesTxt.text = args.ingredientes
    }

}