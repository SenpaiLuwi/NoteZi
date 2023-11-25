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

    //MAY ERROR PA TOH FROM 74-153
    private fun fetchNearestSchedule() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("notezi")

        // Get the current day
        val currentDay = getCurrentDay()

        // Get the current time
        val currentTime = getCurrentTime()

        // Query to find the nearest schedule based on the current day and time
        val query: Query = databaseReference.orderByChild("subjDay").equalTo(currentDay).limitToFirst(1)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { scheduleSnapshot ->
                    val scheduleModel = scheduleSnapshot.getValue(ScheduleModel::class.java)

                    // Check if the schedule's time is later than the current time
                    if (scheduleModel != null && scheduleModel.subjTime?.compareTo(currentTime)!! > 0) {
                        // Display the nearest schedule
                        displaySchedule(scheduleModel)
                        return
                    }
                }

                // If no schedule found for today or with a later time, display a message
                displayNoScheduleMessage()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }


    private fun displaySchedule(scheduleModel: ScheduleModel) {
        schedName.text = scheduleModel.subjName
        schedProf.text = scheduleModel.subjProf
        schedTime.text = scheduleModel.subjTime
        schedDay.text = scheduleModel.subjDay
        schedLink.text = scheduleModel.subjLink
    }

    @SuppressLint("SetTextI18n")
    private fun displayNoScheduleMessage() {
        // Display a message indicating no upcoming schedule for today
        schedName.text = "NO SCHEDULE FOR TODAY!"
        schedProf.text = "⊹⋛⋋( ՞ਊ ՞)⋌⋚⊹"
        schedTime.text = "(づ ◕‿◕ )づ"
        schedDay.text = "( ˘▽˘)っ♨"
        schedLink.text = "( ͡° ͜ʖ ͡° )"
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
}
