package es.unex.giis.asee.gepeto.data.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giis.asee.gepeto.databinding.RecyclerItemListBinding
import java.util.TreeSet

class IngredientesAdapter (
    private var ingredientes_list: TreeSet<String>,
    private val onClick: (ingrediente: String) -> Unit
) : RecyclerView.Adapter<IngredientesAdapter.IngredienteViewHolder>() {

    class IngredienteViewHolder (
        private val binding: RecyclerItemListBinding,
        private val onClick: (ingrediente: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingrediente: String) {
            with(binding) {
                buttonItem.text = ingrediente
                buttonItem.setOnClickListener {
                    onClick(ingrediente)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val binding =
            RecyclerItemListBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )

        return IngredienteViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return ingredientes_list.size
    }

    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
        holder.bind(ingredientes_list.elementAt(position))
    }

    private fun indexOf(ingrediente: String): Int {
        return ingredientes_list.indexOf(ingrediente)
    }

    fun remove(ingrediente: String) {

        ingredientes_list.remove(ingrediente)
        notifyDataSetChanged()
    }

    fun add(ingrediente: String) {

        ingredientes_list.add(ingrediente)
        notifyDataSetChanged()
    }

    fun getList(): TreeSet<String> {
        return ingredientes_list
    }

    fun swap(newIngredientes: TreeSet<String>) {
        ingredientes_list = newIngredientes
        notifyDataSetChanged()
    }
}