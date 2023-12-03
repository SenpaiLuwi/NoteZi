package com.example.notezi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class MainActivityEDITPROFILE : AppCompatActivity() {

    // UI elements
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var userSchoolIDEditText: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var userProfileImageView: CircleImageView

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

        // Set text change listeners for EditText fields to update UI in real-time
        userNameEditText.addTextChangedListener(createTextWatcher())
        userEmailEditText.addTextChangedListener(createTextWatcher())
        userSchoolIDEditText.addTextChangedListener(createTextWatcher())

        // Set a click listener for the "Save Changes" button
        saveChangesButton.setOnClickListener {
            // Retrieve user input from EditTexts
            val userName = userNameEditText.text.toString().takeIf { it.isNotBlank() }
            val userEmail = userEmailEditText.text.toString().takeIf { it.isNotBlank() }
            val userSchoolID = userSchoolIDEditText.text.toString().takeIf { it.isNotBlank() }

            // Use a default profile image URI if no image is selected
            val defaultImageUri = "android.resource://${packageName}/${R.drawable.joshhutchersonpic}"

            // VALIDATION OF THE USER INPUT
            if (userName == null) {
                validateMsg("User Name", this)
            } else if (userEmail == null) {
                validateMsg("Email", this)
            } else if (userSchoolID == null) {
                validateMsg("School ID", this)
            } else {
                // Continue with the save operation since all fields are non-empty

                // Retrieve current user data from the database
                databaseReference.child(currentUserUid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val userFromDatabase: User = dataSnapshot.getValue(User::class.java) ?: return

                            // Use the default image URI if no image is selected
                            val profileImageUri = selectedImageUri?.toString() ?: userFromDatabase.userProfileImageUri

                            // Changes are made, update the user data in the Firebase Realtime Database using the existing user ID
                            val user = User(userName, userEmail, userSchoolID, profileImageUri)
                            databaseReference.child(currentUserUid).setValue(user)

                            // Create an Intent to pass data to MainActivityPROFILE
                            val intent = Intent(this@MainActivityEDITPROFILE, MainActivityPROFILE::class.java).apply {
                                putExtra(MainActivityPROFILE.EXTRA_USER_NAME, userName)
                                putExtra(MainActivityPROFILE.EXTRA_USER_EMAIL, userEmail)
                                putExtra(MainActivityPROFILE.EXTRA_USER_SCHOOL_ID, userSchoolID)
                                putExtra(MainActivityPROFILE.EXTRA_USER_PROFILE_IMAGE_URI, profileImageUri)
                            }

                            // Start MainActivityPROFILE with the provided data
                            startActivity(intent)

                            // Finish the current activity to prevent going back to MainActivityEDITPROFILE when pressing back
                            finish()
                        } else {
                            // No changes are made, show a message or handle it accordingly
                            showToast("No changes made.")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        showToast("Failed to retrieve user data from the database.")
                    }
                })
            }
        }


        // Load user data initially
        loadUserData()
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

    // Function to create a TextWatcher for dynamic updates
    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the UI dynamically
                updateProfileUI(
                    selectedImageUri
                )
            }
        }
    }

// Function to update the UI with the entered user information
    private fun updateProfileUI(imageUri: Uri?) {
        // If an image is selected, update the user profile image view
        if (imageUri != null) {
            userProfileImageView.setImageURI(imageUri)
        } else {
            // If no image is selected, use the default image URL
            val defaultImageUri = Uri.parse("android.resource://${packageName}/${R.drawable.icongithub}")
            userProfileImageView.setImageURI(defaultImageUri)

            // Store the selected image URI as the default image URI
            selectedImageUri = defaultImageUri
        }
    }


    // Function to load user data initially
    private fun loadUserData() {
        databaseReference.child(currentUserUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userFromDatabase: User = dataSnapshot.getValue(User::class.java) ?: return

                    // Load and display the user profile image using Glide
                    Glide.with(this@MainActivityEDITPROFILE)
                        .load(Uri.parse(userFromDatabase.userProfileImageUri))
                        .into(userProfileImageView)

                    // Set the text of the EditText fields with the existing user data
                    userNameEditText.setText(userFromDatabase.userName)
                    userEmailEditText.setText(userFromDatabase.userEmail)
                    userSchoolIDEditText.setText(userFromDatabase.userSchoolID)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Failed to retrieve user data from the database.")
            }
        })
    }

    // Function to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // VALIDATION OF THE USER INPUT
    private fun validateMsg(field: String, context: Context) {
        Toast.makeText(context, "Please Enter the $field", Toast.LENGTH_SHORT).show()
    }
}
