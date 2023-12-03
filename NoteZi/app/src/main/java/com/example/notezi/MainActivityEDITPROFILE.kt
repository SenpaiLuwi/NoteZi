package com.example.notezi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

class MainActivityEDITPROFILE : AppCompatActivity() {

    // UI elements
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var userSchoolIDEditText: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var userProfileImageView: CircleImageView

    // Request code for picking an image
    private val pickImageRequestCode = 123

    // Variable to store the selected image URI
    private var selectedImageUri: Uri? = null

    // Firebase database reference
    private lateinit var databaseReference: DatabaseReference

    // Unique identifier for the user
    private lateinit var currentUserUid: String // Replace this with your user identifier logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_edit_profile)

        // Initialize UI elements
        userNameEditText = findViewById(R.id.edit_userName_id)
        userEmailEditText = findViewById(R.id.edit_userEmail_id)
        userSchoolIDEditText = findViewById(R.id.edit_userSchoolID_id)
        saveChangesButton = findViewById(R.id.edit_userSave_id)
        userProfileImageView = findViewById(R.id.userId_Img)

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        // Replace the following line with your logic to get the current user identifier
        currentUserUid = "NDjkGlDq3678HDdJ3f"

        // Set a click listener for the profile image to pick an image from the gallery
        userProfileImageView.setOnClickListener {
            pickImageFromGallery()
        }

        // Set a click listener for the "Save Changes" button
        saveChangesButton.setOnClickListener {
            // Retrieve user input from EditTexts
            val userName = userNameEditText.text.toString()
            val userEmail = userEmailEditText.text.toString()
            val userSchoolID = userSchoolIDEditText.text.toString()

            // Create a User object with the entered information and the selected image URI
            val user = User(userName, userEmail, userSchoolID, selectedImageUri.toString())

            // Update the user data in the Firebase Realtime Database using the existing user ID
            databaseReference.child(currentUserUid).setValue(user)

            // Create an Intent to pass data to MainActivityPROFILE
            val intent = Intent(this, MainActivityPROFILE::class.java).apply {
                putExtra(MainActivityPROFILE.EXTRA_USER_NAME, userName)
                putExtra(MainActivityPROFILE.EXTRA_USER_EMAIL, userEmail)
                putExtra(MainActivityPROFILE.EXTRA_USER_SCHOOL_ID, userSchoolID)
                putExtra(MainActivityPROFILE.EXTRA_USER_PROFILE_IMAGE_URI, selectedImageUri.toString())
            }

            // Start MainActivityPROFILE with the provided data
            startActivity(intent)
        }
    }

    // Function to start an activity to pick an image from the gallery
    private fun pickImageFromGallery() {
        // Start the activity to pick an image
        getContent.launch("image/*")
    }

    // Activity Result Launcher for image picking
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Set the selected image URI to the user profile image view
            userProfileImageView.setImageURI(uri)
            // Store the selected image URI
            selectedImageUri = uri
        }
}
