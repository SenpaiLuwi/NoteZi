package com.example.notezi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("NAME_SHADOWING")
class MainActivitySchedule : AppCompatActivity() {

    private lateinit var homeBtn: View
    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View
    private lateinit var timeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_schedule)

        // Initialize the buttons
        homeBtn = findViewById(R.id.home_btn)
        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)
        timeView = findViewById(R.id.time_id)

        // Set the current date to the timeView in the desired format
        val currentDate = getCurrentDate()
        timeView.text = currentDate

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        taskBtn.setOnClickListener {
            startActivity(Intent(this, MainActivityTask::class.java))
        }

        schedBtn.setOnClickListener {
            // Update the date again if the user navigates back to this activity
            val currentDate = getCurrentDate()
            timeView.text = currentDate

            startActivity(Intent(this, MainActivitySchedule::class.java))
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(this, MainActivityProfile::class.java))
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }
}
