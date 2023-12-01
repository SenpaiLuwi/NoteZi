package com.example.notezi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import android.net.Uri

class SideABOUT : AppCompatActivity() {

    private lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_about)

        videoView = findViewById(R.id.videoViews)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.noteziabout
        val uri = Uri.parse(videoPath)

        // Set video URI and start playing
        videoView.setVideoURI(uri)
        videoView.start()

        // Loop the video
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
        }



    }
}