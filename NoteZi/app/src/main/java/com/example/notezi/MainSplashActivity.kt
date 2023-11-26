package com.example.notezi

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class MainSPLASHACTIVITY : AppCompatActivity() {

    // Variables Used
    private lateinit var progBar: ProgressBar
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

    private val textChangeHandler = Handler()

    // Start of OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        // Initialize variables
        progBar = findViewById(R.id.progBar_id)
        splashText = findViewById(R.id.splashText_id)

        // Start changing text every 3 seconds
        startTextChange()

        // Loading delay of 15 secs
        Handler().postDelayed({
            checkInternetAndNavigate()
        }, 15000)
    }

    // Start changing text every 3 seconds
    private fun startTextChange() {
        textChangeHandler.postDelayed(object : Runnable {
            override fun run() {
                splashText.text = getRandomSentence()
                textChangeHandler.postDelayed(this, 3000)
            }
        }, 3000)
    }

    // Stop changing text when the activity is destroyed
    override fun onDestroy() {
        textChangeHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
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
                    "Asking Rotsen if he is OK?",
                    "Waking up Miguel",
                    "Fixing the Bugs",
                    "Making the App Pretty",
                    "Optimizing the Notes",
                    "Sit Tight",

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
            val intent = Intent(this, MainActivityHOME::class.java)
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
