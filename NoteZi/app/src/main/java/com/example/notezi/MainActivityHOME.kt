package com.example.notezi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.PriorityQueue

class MainActivityHOME : AppCompatActivity() {

    private lateinit var homeBtn: View
    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View

    // TEXTVIEWS TO DISPLAY SCHEDULE INFORMATION
    private lateinit var schedName: TextView
    private lateinit var schedProf: TextView
    private lateinit var schedTime: TextView
    private lateinit var schedDay: TextView
    private lateinit var schedLink: TextView

    // Use a PriorityQueue to keep track of schedules based on time
    private val scheduleQueue: PriorityQueue<ScheduleModel> = PriorityQueue(compareBy { it.subjTime })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the buttons
        homeBtn = findViewById(R.id.home_btn)
        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)

        // Initialize TextViews
        schedName = findViewById(R.id.sched_name_id)
        schedProf = findViewById(R.id.sched_prof_id)
        schedTime = findViewById(R.id.sched_time_id)
        schedDay = findViewById(R.id.sched_day_id)
        schedLink = findViewById(R.id.sched_link_id)

        homeBtn.setOnClickListener {
            val myIntent = Intent(this, MainActivityHOME::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        taskBtn.setOnClickListener {
            val myIntent = Intent(this, MainActivityTASK::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        schedBtn.setOnClickListener {
            val myIntent = Intent(this, MainActivitySCHEDULE::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        profileBtn.setOnClickListener {
            val myIntent = Intent(this, MainActivityPROFILE::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        // Fetch the nearest schedule
        fetchNearestSchedule()
    }

    private fun fetchNearestSchedule() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("notezi")
        val currentDay = getCurrentDay()
        val currentTime = getCurrentTime()

        val query: Query = databaseReference.orderByChild("subjDay").equalTo(currentDay)

        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                updateNearestSchedule(snapshot, currentTime)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                updateNearestSchedule(snapshot, currentTime)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                updateNearestSchedule(snapshot, currentTime) // Check for the next nearest schedule
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle movement if needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }


    private fun updateNearestSchedule(snapshot: DataSnapshot, currentTime: String) {
        val scheduleModel = snapshot.getValue(ScheduleModel::class.java)

        if (scheduleModel != null) {
            scheduleQueue.add(scheduleModel)

            // Check if the schedule is upcoming based on the user's phone time
            if (currentTime >= scheduleModel.subjTime) {
                displaySchedule(scheduleModel)
            } else {
                // If the current time is after the schedule's end time, check for the next schedule
                scheduleQueue.remove(scheduleModel)
                if (!scheduleQueue.isEmpty()) {
                    scheduleQueue.peek()?.let { displaySchedule(it) }
                } else {
                    // Clear the TextViews when there are no more upcoming schedules
                    clearTextViews()
                }
            }
        } else {
            // Clear the TextViews when no schedule is found
            clearTextViews()
        }
    }

    private fun displaySchedule(scheduleModel: ScheduleModel) {
        schedName.text = scheduleModel.subjName
        schedProf.text = scheduleModel.subjProf
        schedTime.text = scheduleModel.subjTime
        schedDay.text = scheduleModel.subjDay
        schedLink.text = scheduleModel.subjLink
    }

    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()

        // Convert day of the week to a readable format
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Get the current time in 12-hour format
        return dateFormat.format(calendar.time)
    }

    @SuppressLint("SetTextI18n")
    private fun clearTextViews() {
        schedName.text = "NO SCHEDULE FOR TODAY!"
        schedProf.text = "⊹⋛⋋( ՞ਊ ՞)⋌⋚⊹"
        schedTime.text = "(づ ◕‿◕ )づ"
        schedDay.text = "( ˘▽˘)っ♨"
        schedLink.text = "( ͡° ͜ʖ ͡° )"
    }
}
