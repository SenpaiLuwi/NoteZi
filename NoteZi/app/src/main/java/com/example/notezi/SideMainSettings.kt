package com.example.notezi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SideMainSettings : AppCompatActivity() {

    private lateinit var editprofile: TextView
    private lateinit var settingabout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_main_settings)

        editprofile = findViewById(R.id.editprofile_id)
        settingabout = findViewById(R.id.settingabout_id)

        editprofile.setOnClickListener {
            startActivity(Intent(this, MainActivityProfileEdit::class.java))
        }

        settingabout.setOnClickListener {
            startActivity(Intent(this, SideAbout::class.java))
        }
    }
}