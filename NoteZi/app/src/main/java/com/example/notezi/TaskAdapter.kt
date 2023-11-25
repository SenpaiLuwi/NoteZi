package com.example.notezi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class TaskAdapter(options: FirebaseRecyclerOptions<TaskModel>) :
    FirebaseRecyclerAdapter<TaskModel, TaskAdapter.MyViewHolder>(options) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: TaskModel) {
        holder.courseName.text = model.course
        holder.taskName.text = model.task
        holder.deadline.text = model.deadline
        holder.link.text = model.link
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.side_main_task_list, parent, false) // Replace with the appropriate layout
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.task_course_id) // Change to the appropriate ID
        val taskName: TextView = itemView.findViewById(R.id.task_name_id) // Change to the appropriate ID
        val deadline: TextView = itemView.findViewById(R.id.task_deadline_id) // Change to the appropriate ID
        val link: TextView = itemView.findViewById(R.id.task_link_id) // Change to the appropriate ID
    }
}
