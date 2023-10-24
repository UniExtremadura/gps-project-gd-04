package es.unex.giis.asee.gepeto.view.home.recetas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giis.asee.gepeto.databinding.RecetaItemListBinding
import es.unex.giis.asee.gepeto.model.Receta

class HistorialAdapter(
    private val recetas: List<Receta>,
    private val onClick: (receta: Receta) -> Unit,
    private val onLongClick: (title: Receta) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.RecetaViewHolder>() {

    class RecetaViewHolder(
        private val binding: RecetaItemListBinding,
        private val onClick: (receta: Receta) -> Unit,
        private val onLongClick: (title: Receta) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: Receta, totalItems: Int) {
            with(binding) {
                recetaNombre.text = receta.nombre
                recetaIngredientes.text = receta.getIngredientes()
                recetaImg.setImageResource(receta.imagen)
                clItem.setOnClickListener {
                    onClick(receta)
                }
                clItem.setOnLongClickListener {
                    onLongClick(receta)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val binding =
            RecetaItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecetaViewHolder(binding, onClick, onLongClick)
    }

    override fun getItemCount() = recetas.size

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(recetas[position], recetas.size)
    }

}