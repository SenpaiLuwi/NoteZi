package com.example.notezi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class SideMainUPDATETASK : AppCompatActivity() {

    // VARIABLES
    private lateinit var txtCourse: EditText
    private lateinit var txtTask: EditText
    private lateinit var txtDeadline: EditText
    private lateinit var txtLink: EditText
    private lateinit var btnSave: Button

    // FUNCTIONS INSIDE KT.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_main_update_task)

        txtCourse = findViewById(R.id.edit_task_course)
        txtTask = findViewById(R.id.edit_task_name)
        txtDeadline = findViewById(R.id.edit_task_deadline)
        txtLink = findViewById(R.id.edit_task_link)
        btnSave = findViewById(R.id.btn_id_update)

        btnSave.setOnClickListener {
            saveTaskToFirebase()
        }
    }

    private fun saveTaskToFirebase() {
        val course = txtCourse.text.toString().trim()
        val task = txtTask.text.toString().trim()
        val deadline = txtDeadline.text.toString().trim()
        val link = txtLink.text.toString().trim()

        if (course.isNotEmpty() && task.isNotEmpty() && deadline.isNotEmpty() && link.isNotEmpty()) {
            // SAVE DATA TO FIREBASE
            val taskRef = FirebaseDatabase.getInstance().reference.child("notezi").child("tasks").push()
            val taskModel = TaskModel(course, task, deadline, link)
            taskRef.setValue(taskModel)

            // CLEAR INPUT AFTER SAVED.
            clearInputFields()

            // NAVIGATE BACK TO MainActivityTASK
            navigateToMainActivity()

        } else {
            Toast.makeText(this, "PLEASE, FILL IN ALL FIELDS. THANK YOU.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        txtCourse.text.clear()
        txtTask.text.clear()
        txtDeadline.text.clear()
        txtLink.text.clear()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivityTASK::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}
