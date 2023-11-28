package com.example.notezi

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

    // FUNCTION FOR THE CLICK-LISTENER FOR EVERY EACH ITEM ON THE RECYCLERVIEW:
    private var onItemClickListener: ((TaskModel) -> Unit)? = null
    private var onItemLongPressListener: ((TaskModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (TaskModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongPressListener(listener: (TaskModel) -> Unit) {
        onItemLongPressListener = listener
    }

    // VIEW HOLDER FOR EACH ITEM ON THE RECYCLERVIEW:
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTextView: TextView = itemView.findViewById(R.id.task_course_id)
        val nameTextView: TextView = itemView.findViewById(R.id.task_name_id)
        val deadlineTextView: TextView = itemView.findViewById(R.id.task_deadline_id)
        val linkTextView: TextView = itemView.findViewById(R.id.task_link_id)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // NOT SURE CORRECT EXPLANATION:
                    // CALL LISTENER IF NOT NULL AND PASS TO CLICKED TASK:
                    onItemClickListener?.invoke(getItem(position))
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongPressListener?.invoke(getItem(position))
                }
                true
            }
        }
    }

    // FUNCTION FOR CREATE NEW VIEW HOLDER INSTANCES, INFLATES TO LAYOUT (SIDE__MAIN_TASK_LIST)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.side_main_task_list, parent, false)
        return TaskViewHolder(view)
    }

    // FUNCTION FOR CALL THE RECYCLERVIEW TO DISPLAY THE DATA:
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int, model: TaskModel) {
        // Bind data to the ViewHolder
        holder.courseTextView.text = model.taskCourse
        holder.nameTextView.text = model.taskName
        holder.deadlineTextView.text = model.taskDeadline
        holder.linkTextView.text = model.taskLink
    }
}