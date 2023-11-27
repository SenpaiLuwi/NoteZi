package com.example.notezi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class SideMainUPDATETASK : AppCompatActivity() {

    // VARIABLES
    private lateinit var txtCourse: EditText
    private lateinit var txtTask: EditText
    private lateinit var txtDeadline: EditText
    private lateinit var txtLink: EditText
    private lateinit var btnSave: Button

    private var isEditing = false
    private var taskId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_main_update_task)

        initializeUI()

        // FUNCTION IF EDIT OPERATION
        if (intent.hasExtra("TASK_MODEL")) {
            val taskModel = intent.getParcelableExtra<TaskModel>("TASK_MODEL")


            if (taskModel != null) {
                isEditing = true
                taskId = taskModel.taskId
                displayTaskDetails(taskModel)
            } else {
                // YOU CAN ADD TOAST HERE MSG "ERROR"
            }
        }

        // FUNCTION FOR CLICK LISTENER SAVE BUTTON, DIRECT TO SAVE DATABASE ALSO.
        btnSave.setOnClickListener {
            saveTaskToFirebase()
        }
    }


    // FUNCTION FOR INITIATLIZE UI ETC
    private fun initializeUI() {
        txtCourse = findViewById(R.id.edit_task_course)
        txtTask = findViewById(R.id.edit_task_name)
        txtDeadline = findViewById(R.id.edit_task_deadline)
        txtLink = findViewById(R.id.edit_task_link)
        btnSave = findViewById(R.id.btn_id_update)
    }

    // FUNCTION ON DISPLAY TASK DETAILS AND ALSO FOR EDIT THE USER
    private fun displayTaskDetails(taskModel: TaskModel) {
        txtCourse.setText(taskModel.taskCourse)
        txtTask.setText(taskModel.taskName)
        txtDeadline.setText(taskModel.taskDeadline)
        txtLink.setText(taskModel.taskLink)
    }

    // FUNCTION FOR SAVE AND ALSO UPDATE TASK TO FIREBASE (DATABASE)
    private fun saveTaskToFirebase() {
        val course = txtCourse.text.toString().trim()
        val task = txtTask.text.toString().trim()
        val deadline = txtDeadline.text.toString().trim()
        val link = txtLink.text.toString().trim()

        if (course.isNotEmpty() && task.isNotEmpty() && deadline.isNotEmpty() && link.isNotEmpty()) {
            if (isEditing) {

                // UPDATE THE EXISTING DETAIL IN TASK
                updateTaskInFirebase(course, task, deadline, link)
            } else {

                // SAVE DATA TO DATABASE
                addTaskToFirebase(course, task, deadline, link)
            }

            // INTENT TO MAINACTIVITYTASK
            navigateToMainActivity()
        } else {
            showToast("PLEASE, FILL UP ALL THE FIELDS, THANK YOU.")
        }
    }

    // FUNCTION FOR ADD A NEW TASK ON FIREBASE (DATABASE)
    private fun addTaskToFirebase(course: String, task: String, deadline: String, link: String) {

        // SAVE TASK
        val databaseReference = FirebaseDatabase.getInstance().reference.child("tasks")
        // GENERATE KEY
        val taskId = databaseReference.push().key
        val taskModel = TaskModel(taskId, course, task, deadline, link)
        databaseReference.child(taskId ?: "").setValue(taskModel)
    }

    // FUNCTION FOR UPDATING OF EXISTING TASK ON DATABASE
    private fun updateTaskInFirebase(course: String, task: String, deadline: String, link: String) {
        // Check if taskId is not null
        taskId?.let { taskId ->
            val taskRef = FirebaseDatabase.getInstance().reference.child("tasks").child(taskId)
            val updatedTaskModel = TaskModel(taskId, course, task, deadline, link)
            taskRef.setValue(updatedTaskModel)
        }
    }

    // NAVIGATE OR INTENT TO THE MAINACTIVITYTASK
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivityTASK::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    // DIKO ALAM PARA SAAN TO PERO KASAMA TO SA CODE:
    private fun showToast(message: String) {
        // ADD SOME TOAST MSG HERE:
    }
}
