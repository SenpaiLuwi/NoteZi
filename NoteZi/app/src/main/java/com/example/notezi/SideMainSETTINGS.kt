package com.example.notezi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class SideMainSETTINGS : AppCompatActivity() {

    private lateinit var editprofile: TextView
    private lateinit var settingabout: TextView
    private lateinit var settinghelp: TextView
    private lateinit var settingsecret: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_main_settings)

        editprofile = findViewById(R.id.editprofile_id)
        settingabout = findViewById(R.id.settingabout_id)
        settinghelp = findViewById(R.id.settinghelp_id)
        settingsecret = findViewById(R.id.hidden_btn)

        editprofile.setOnClickListener {
            startActivity(Intent(this, MainActivityEDITPROFILE::class.java))
        }

        settingabout.setOnClickListener {
            startActivity(Intent(this, SideABOUT::class.java))
        }

        settinghelp.setOnClickListener {
           startActivity(Intent(this, SideHelp::class.java))
        }

        settingsecret.setOnClickListener{
            startActivity(Intent(this, SideSecret::class.java))

        }
    }
}