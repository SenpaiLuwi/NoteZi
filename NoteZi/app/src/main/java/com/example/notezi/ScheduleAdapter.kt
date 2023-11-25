package com.example.notezi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

// Class for ScheduleAdapter.
// THIS WORKS WITH FIREBASE REALTIME DATABASE AND SIMPLIFIES THE PROCESS OF POPULATING A RECYCLERVIEW WITH DATA FROM THE DATABASE.
class ScheduleAdapter(options: FirebaseRecyclerOptions<ScheduleModel>) :
    FirebaseRecyclerAdapter<ScheduleModel, ScheduleAdapter.MyViewHolder>(options) {

    // START OF onBindViewHolder
    // RESPONSIBLE FOR BINDING THE DATA FROM THE SCHEDULEMODEL TO THE VIEWS INSIDE EACH ITEM OF THE RECYCLERVIEW
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ScheduleModel) {
        holder.schedName.text = model.subjName
        holder.schedProf.text = model.subjProf
        holder.schedTime.text = model.subjTime
        holder.schedDay.text = model.subjDay
        holder.schedLink.text = model.subjLink
    }

    // START OF onCreateViewHolder
    // IT INFLATES A LAYOUT (R.LAYOUT.SIDE_MAIN_SCHEDULE_LIST) TO CREATE THE INDIVIDUAL ITEMS IN THE LIST.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.side_main_schedule_list, parent, false)
        return MyViewHolder(view)
    }

    // START OF MyViewHolder
    // HOLDS REFERENCES TO THE TEXTVIEW
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schedName: TextView = itemView.findViewById(R.id.sched_name_id)
        val schedProf: TextView = itemView.findViewById(R.id.sched_prof_id)
        val schedTime: TextView = itemView.findViewById(R.id.sched_time_id)
        val schedDay: TextView = itemView.findViewById(R.id.sched_day_id)
        val schedLink: TextView = itemView.findViewById(R.id.sched_link_id)
    }
}
