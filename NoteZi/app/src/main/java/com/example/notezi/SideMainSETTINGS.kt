package com.example.notezi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class SideMainSETTINGS : AppCompatActivity() {

    private lateinit var editprofile: TextView
    private lateinit var settingabout: TextView
    private lateinit var settingsecret: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_main_settings)

        editprofile = findViewById(R.id.editprofile_id)
        settingabout = findViewById(R.id.settingabout_id)
        settingsecret = findViewById(R.id.hidden_btn)

        editprofile.setOnClickListener {
            Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivityEDITPROFILE::class.java))
        }

        settingabout.setOnClickListener {
            Toast.makeText(this, "About NoteZi", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SideABOUT::class.java))
        }

        settingsecret.setOnClickListener{
            Toast.makeText(this, "Josh Hutcherson", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SideSecret::class.java))

        }
    }
}