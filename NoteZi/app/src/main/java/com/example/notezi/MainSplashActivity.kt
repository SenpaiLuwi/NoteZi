package com.example.notezi

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class MainSplashActivity : AppCompatActivity() {

    // Variables Used
    private lateinit var progBar: ProgressBar
    private lateinit var imageViewOne: ImageView
    private lateinit var imageViewTwo: ImageView
    private lateinit var imageViewThree: ImageView
    private lateinit var imageViewFour: ImageView
    private lateinit var splashText: TextView

    private val sentences = mutableListOf(
        "Checking the Internet Connection",
        "Gathering Data from FireBase",
        "Checking if Louie is Handsome",
        "Fixing the Bugs",
        "Making the App Pretty",
        "Optimizing the Notes",
        "Sit Tight"
    )

    // Start of OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        // Initialize variables
        progBar = findViewById(R.id.progBar_id)
        imageViewOne = findViewById(R.id.imageViewOne)
        imageViewTwo = findViewById(R.id.imageViewTwo)
        imageViewThree = findViewById(R.id.imageViewThree)
        imageViewFour = findViewById(R.id.imageViewFour)
        splashText = findViewById(R.id.splashText_id)

        // Start animations with delays
        startAnimation(imageViewOne, 0)
        startAnimation(imageViewTwo, 1500)
        startAnimation(imageViewThree, 3000)
        startAnimation(imageViewFour, 4500)

        // Loading delay of 10 secs
        Handler().postDelayed({
            checkInternetAndNavigate()
        }, 15000)
    }

    // Start of StartAnimation
    private fun startAnimation(imageView: ImageView, delay: Long) {
        Handler().postDelayed({
            animateUpDown(imageView)
        }, delay)
    }

    // Start of Animation for Up and Down
    private fun animateUpDown(imageView: ImageView) {
        val animation = TranslateAnimation(0f, 0f, 0f, 20f)
        animation.duration = 500
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
        imageView.startAnimation(animation)
        // Change splashText text randomly for each animation
        splashText.text = getRandomSentence()
    }

    // Start of Getting the Random Sentences
    private fun getRandomSentence(): String {
        if (sentences.isEmpty()) {
            // If all sentences have been used, reset the list
            sentences.addAll(
                listOf(
                    "Checking the Internet Connection",
                    "Gathering Data from FireBase",
                    "Checking if Louie is Handsome",
                    "Fixing the Bugs",
                    "Making the App Pretty",
                    "Optimizing the Notes",
                    "Sit Tight"
                )
            )
        }
        val randomIndex = (0 until sentences.size).random()
        return sentences.removeAt(randomIndex)
    }

    // Checking of Internet Connection
    private fun checkInternetAndNavigate() {
        // Check internet connection
        if (isNetworkAvailable()) {
            // Intent to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showNoInternetDialog()
        }
    }

    // Validation of Internet Connection
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected
    }

    // Prompt of Internet Dialog Box
    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please Connect to the Internet to Access NoteZi.")
        builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
            // Close the app if there's no internet connection
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}
