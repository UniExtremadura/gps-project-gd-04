package es.unex.giis.asee.gepeto.adapters

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.databinding.RecyclerTodoItemBinding
import es.unex.giis.asee.gepeto.utils.Tuple

class TodoAdapter (
    private val todoMap: HashMap<String, Boolean>,
    private val onClick: (item: String) -> Unit,
    private val onLongClick: (item: String) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder (
        private val binding: RecyclerTodoItemBinding,
        private val onClick: (item: String) -> Unit,
        private val onLongClick: (item: String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: String, isChecked: Boolean) {
            with(binding) {
                todoItemTxt.text = item
                cbTodoItem.isChecked = isChecked

                if (isChecked) {
                    // Aplicar tachado y estilo cursiva al TextView
                    todoItemTxt.paintFlags = todoItemTxt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    todoItemTxt.setTypeface(null, Typeface.ITALIC)
                    cbTodoItem.isEnabled = false
                } else {
                    // Estilo normal del TextView
                    todoItemTxt.paintFlags = todoItemTxt.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    todoItemTxt.setTypeface(null, Typeface.NORMAL)
                    cbTodoItem.isEnabled = true
                }

                cbTodoItem.setOnClickListener {
                    onClick(item)
                }

                cvTodoItem.setOnLongClickListener {
                    onLongClick(item)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            RecyclerTodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return TodoViewHolder(binding, onClick, onLongClick)
    }

    override fun getItemCount(): Int {
        return todoMap.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = todoMap.keys.toList()[position]
        val isChecked = todoMap[currentItem] ?: false
        holder.bind(currentItem, isChecked)
    }



    fun add(item: String) {
        todoMap[item] = false
        Session.setValue("todoList", todoMap)
        notifyDataSetChanged()
    }

    fun remove(item: String) {
        todoMap.remove(item)
        Session.setValue("todoList", todoMap)
        notifyDataSetChanged()
    }

    fun swap(newItemMap: HashMap<String, Boolean>) {
        todoMap.clear()
        todoMap.putAll(newItemMap)
        notifyDataSetChanged()
    }

    fun check(item: String) {
        todoMap[item] = true
        notifyDataSetChanged()
    }
}