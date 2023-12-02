package com.example.notezi

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

@Suppress("DEPRECATION")
class TaskAdapter(options: FirebaseRecyclerOptions<TaskModel>) :
    FirebaseRecyclerAdapter<TaskModel, TaskAdapter.TaskViewHolder>(options) {

    // CLICKABLE FOR RECYCLER VIEW ITEMS
    private var onItemClickListener: ((TaskModel) -> Unit)? = null
    private var onItemLongPressListener: ((TaskModel) -> Unit)? = null

    // FUNCTION SET LISTENER FOR OnItemClickListener
    fun setOnItemClickListener(listener: (TaskModel) -> Unit) {
        onItemClickListener = listener
    }

    // FUNCTION SET LISTENER FOR LONG PRESS in OnItemClickListener
    fun setOnItemLongPressListener(listener: (TaskModel) -> Unit) {
        onItemLongPressListener = listener
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // VIEW THE RECYCLERVIEW ITEM
        private val courseTextView: TextView = itemView.findViewById(R.id.task_course_id)
        private val nameTextView: TextView = itemView.findViewById(R.id.task_name_id)
        private val typeTextView: TextView = itemView.findViewById(R.id.task_type_id)
        private val deadlineTextView: TextView = itemView.findViewById(R.id.task_deadline_id)
        private val linkTextView: TextView = itemView.findViewById(R.id.task_link_id)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(getItem(position))
                }
            }

            // Item long press listener
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongPressListener?.invoke(getItem(position))
                }
                true
            }
        }

        // BINDING THE DATA TO THE VIEWS
        fun bind(taskModel: TaskModel) {
            courseTextView.text = taskModel.taskCourse
            nameTextView.text = taskModel.taskName
            typeTextView.text = taskModel.taskType
            linkTextView.text = taskModel.taskLink

            // DISPLAYING THE HTML FORMAT TEXT FOR DEADLINE (IF APPLICABLE)
            deadlineTextView.text =
                Html.fromHtml(taskModel.taskDeadline, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    // CREATE A NEW VIEW HOLDER WHEN ALSO NEEDED
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.side_main_task_list, parent, false)
        return TaskViewHolder(view)
    }

    // BINDING THE DATA TO THE VIEW HOLDER
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int, model: TaskModel) {
        holder.bind(model)
    }
}
