package com.example.notezi

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton



class MainActivityTASK : AppCompatActivity() {

    private lateinit var homeBtn: View
    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task)


        // Initialize the buttons
        homeBtn = findViewById(R.id.home_btn)
        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)


        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivityHOME::class.java))
        }

        taskBtn.setOnClickListener {
            startActivity(Intent(this, MainActivityTASK::class.java))
        }

        schedBtn.setOnClickListener {
            startActivity(Intent(this, MainActivitySCHEDULE::class.java))
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(this, MainActivityPROFILE::class.java))
        }


    }

}
