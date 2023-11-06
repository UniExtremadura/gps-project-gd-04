package es.unex.giis.asee.gepeto.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giis.asee.gepeto.databinding.RecyclerSwapItemBinding
import es.unex.giis.asee.gepeto.model.Ingrediente
import java.util.TreeSet

class ItemSwapAdapter(
    private var itemSet: TreeSet<String>,
    private val onClick: (item: String) -> Unit,

    private var ingredients: List<Ingrediente> = emptyList()

) : RecyclerView.Adapter<ItemSwapAdapter.ItemViewHolder>() {

    class ItemViewHolder (
        private val binding: RecyclerSwapItemBinding,
        private val onClick: (item: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                buttonItem.text = item
                buttonItem.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            RecyclerSwapItemBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ItemViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return itemSet.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemSet.elementAt(position))
    }

    private fun indexOf(item: String): Int {
        return itemSet.indexOf(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(item: String) {
        itemSet.remove(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(item: String) {
        itemSet.add(item)
        notifyDataSetChanged()
    }

    fun getList(): TreeSet<String> {
        return itemSet
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swap(newItemSet: TreeSet<String>) {
        itemSet = newItemSet
        notifyDataSetChanged()
    }

    fun getSet(): TreeSet<String> {
        return itemSet
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newIngredients: List<Ingrediente>) {
        this.ingredients = newIngredients
        notifyDataSetChanged()
    }


}