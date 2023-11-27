package com.example.notezi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView

class MainActivityTASK : AppCompatActivity() {

    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View
    private lateinit var addBtn: ImageView

    //DAGDAG KO:
    private lateinit var mainAdapter: TaskAdapter
    private lateinit var searchTask: SearchView
    private lateinit var filerTask: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task)


        // Initialize the buttons

        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)
        addBtn = findViewById(R.id.add_btn)



        addBtn.setOnClickListener{
            val myIntent = Intent(this, SideMainUPDATETASK::class.java)
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


    }

}
