package es.unex.giis.asee.gepeto.view.home.recetas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.unex.giis.asee.gepeto.GepetoApplication
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.adapters.RecetasAdapter
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.databinding.FragmentHistorialBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.utils.filtrarLista
import es.unex.giis.asee.gepeto.utils.filtrarReceta
import es.unex.giis.asee.gepeto.utils.ocultarBottomNavigation
import es.unex.giis.asee.gepeto.view.home.HomeActivity
import kotlinx.coroutines.launch
import java.util.TreeSet


class HistorialFragment : Fragment() {

    var recetas: List<Receta> = emptyList()

    private lateinit var repository: Repository
    private lateinit var listener: OnRecetaClickListener
    interface OnRecetaClickListener {
        fun onRecetaClick(receta: Receta)
    }

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecetasAdapter

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is OnRecetaClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val appContainer = (this.activity?.application as GepetoApplication).appContainer
        repository = appContainer.repository

        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadRecetasFromDB()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

    private fun loadRecetasFromDB() {
        lifecycleScope.launch {
            val user = Session.getValue("user") as User
            //recetas = db.recetaDao().getUserConRecetas(user.userId!!).recetas
            recetas = repository.getHistorial(user.userId!!)
            binding.spinner.visibility = View.GONE

            if ( recetas.isEmpty() ) {
                binding.buscadorRecetaContainer.visibility = View.GONE
                binding.noHayRecetas.visibility = View.VISIBLE
            } else {
                binding.noHayRecetas.visibility = View.GONE
                binding.buscadorRecetaContainer.visibility = View.VISIBLE
            }

            filtrarReceta(
                binding.buscadorDeRecetas,
                recetas,
                adapter
            )

            adapter.updateData(recetas)
        }
    }

    private fun setUpRecyclerView() {
        adapter = RecetasAdapter(recetas = emptyList(),
            onClick = {
                listener.onRecetaClick(it)
            },
            onLongClick = {
                setRecetaFav(it)
            }
            , context = context
        )
        with(binding) {
            rvHistorialList.layoutManager = LinearLayoutManager(context)
            rvHistorialList.adapter = adapter
        }
    }

    private fun setRecetaFav(receta: Receta) {

        if (receta.favorita) return

        lifecycleScope.launch {
            receta.favorita = !receta.favorita
            //db.recetaDao().update(receta)
            repository.recipeToLibrary(receta, (Session.getValue("user") as User).userId!!)
            Toast.makeText(context, "Receta añadida a favoritos!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}