package com.example.notezi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class MainActivityPROFILE : AppCompatActivity() {

    // Declare UI elements
    private lateinit var taskBtn: ImageButton
    private lateinit var schedBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var settingsBtn: ImageButton

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userSchoolIDTextView: TextView
    private lateinit var userProfileImageView: CircleImageView

    // Firebase database references
    private lateinit var usersDatabaseReference: DatabaseReference
    private lateinit var currentUserUid: String // Replace this with your user identifier logic

    companion object {
        // Constants for passing data between activities
        const val EXTRA_USER_NAME = "com.example.notezi.EXTRA_USER_NAME"
        const val EXTRA_USER_EMAIL = "com.example.notezi.EXTRA_USER_EMAIL"
        const val EXTRA_USER_SCHOOL_ID = "com.example.notezi.EXTRA_USER_SCHOOL_ID"
        const val EXTRA_USER_PROFILE_IMAGE_URI = "com.example.notezi.EXTRA_USER_PROFILE_IMAGE_URI"
    }

    // Use a default profile image URI if no image is selected
    val defaultImageUri = "android.resource://${packageName}/${R.drawable.joshhutchersonpic}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profile)

        // Initialize UI elements
        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)
        settingsBtn = findViewById(R.id.settings_btn)

        userNameTextView = findViewById(R.id.userId_txt)
        userEmailTextView = findViewById(R.id.userEmail_txt)
        userSchoolIDTextView = findViewById(R.id.userSchoolID_txt)
        userProfileImageView = findViewById(R.id.userId_Img)

        // Firebase ID
        currentUserUid = "NDjkGlDq3678HDdJ3f"

        // Firebase database reference
        usersDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val query: Query = usersDatabaseReference.child(currentUserUid)

        // Read data from Firebase
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user data from the database
                    val userFromDatabase: User = dataSnapshot.getValue(User::class.java) ?: return

                    // Update UI with user data
                    userNameTextView.text = userFromDatabase.userName
                    userEmailTextView.text = userFromDatabase.userEmail
                    userSchoolIDTextView.text = userFromDatabase.userSchoolID

                    // Load and display the user profile image using Glide
                    Glide.with(this@MainActivityPROFILE)
                        .load(Uri.parse(userFromDatabase.userProfileImageUri))
                        .into(userProfileImageView)
                } else {
                    showToast("Failed to retrieve user data from the database.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Failed to retrieve user data from the database.")
            }
        })

        // Button click listeners for navigation
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

        // Navigate to the Settings activity
        settingsBtn.setOnClickListener {
            startActivity(Intent(this, SideMainSETTINGS::class.java))
        }
    }
    // Display a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
