package es.unex.giis.asee.gepeto.view.home.recetas

import android.content.Context
import es.unex.giis.asee.gepeto.adapters.ViewPagerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.databinding.FragmentRecetasBinding
import es.unex.giis.asee.gepeto.view.home.IngredientesFragment
import java.lang.RuntimeException
import java.util.TreeSet


class RecetasFragment : Fragment() {

    private var _binding: FragmentRecetasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentRecetasBinding.inflate(inflater, container, false)
        setUpTabs()

        // Accion del boton: Crear Receta. Redirige a IngredientesFragment
        binding.crearRecetaButton.setOnClickListener {
            findNavController().navigate(R.id.ingredientesFragment)
        }

        return binding.root
    }

    private fun setUpTabs() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.pager

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Historial"
                1 -> tab.text = "Favoritas"
            }
        }.attach()
    }
}
