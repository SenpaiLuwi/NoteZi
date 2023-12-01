package com.example.notezi

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SideSecret : AppCompatActivity() {

    private lateinit var bgVideo: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_secret)

        bgVideo = findViewById(R.id.joshVid)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.josh
        val uri = Uri.parse(videoPath)

        // Set video URI and start playing
        bgVideo.setVideoURI(uri)

        // Add media controller
        val mediaController = MediaController(this)
        mediaController.setAnchorView(bgVideo)
        bgVideo.setMediaController(mediaController)

        // Set audio focus
        bgVideo.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(1f, 1f) // Set volume (left, right) to maximum
        }

        // Loop the video
        bgVideo.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        bgVideo.setMediaController(null)
        bgVideo.start()


    }
}
