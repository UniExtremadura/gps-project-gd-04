package es.unex.giis.asee.gepeto.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.databinding.RecyclerTodoItemBinding
import es.unex.giis.asee.gepeto.utils.Tuple


class TodoAdapter (
    private val todoList: MutableList<Tuple<String, Boolean>>,
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
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = todoList[position].first
        val isChecked = todoList[position].second
        holder.bind(currentItem, isChecked)
    }



    fun add(item: String) {
        todoList.add(Tuple(item, false))
        Session.setValue("todoList", todoList)
        notifyDataSetChanged()
    }

    fun remove(item: String) {
        val index = todoList.map { it.first }.toMutableList().indexOf(item)
        todoList.removeAt(index)
        Session.setValue("todoList", todoList)
        notifyDataSetChanged()
    }

    fun swap(newItemSet: List<Tuple<String, Boolean>>) {
        todoList.clear()
        todoList.addAll(newItemSet)
        notifyDataSetChanged()
    }

    fun check(item: String) {
        val index = todoList.indexOf(Tuple(item, false))
        todoList[index].second = true
        notifyDataSetChanged()
    }
}