package es.unex.giis.asee.gepeto.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.databinding.RecetaItemListBinding
import es.unex.giis.asee.gepeto.model.Receta
import java.util.TreeSet

class RecetasAdapter(
    private var recetas: List<Receta>,
    private val onClick: (receta: Receta) -> Unit,
    private val onLongClick: (title: Receta) -> Unit,
    private val context: Context?
    ) : RecyclerView.Adapter<RecetasAdapter.RecetaViewHolder>() {

    class RecetaViewHolder(
        private val binding: RecetaItemListBinding,
        private val onClick: (receta: Receta) -> Unit,
        private val onLongClick: (title: Receta) -> Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: Receta, totalItems: Int) {
            with(binding) {
                recetaNombre.text = receta.nombre
                recetaIngredientes.text = receta.getIngredientesPreview()

                //parsea la imagen a int

                //recetaImg.setImageResource(receta.imagen)
                context?.let {
                    Glide.with(context)
                        .load(receta.imagenPath)
                        .placeholder(R.drawable.ejemplo_plato)
                        .into(recetaImg)
                }
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
        return RecetaViewHolder(binding, onClick, onLongClick, context)
    }

    override fun getItemCount() = recetas.size

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(recetas[position], recetas.size)
    }

    fun updateData(newRecetas: List<Receta>) {
        this.recetas = newRecetas
        notifyDataSetChanged()
    }
}