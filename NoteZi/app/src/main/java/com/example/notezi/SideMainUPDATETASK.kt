package com.example.notezi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class SideMainUPDATETASK : AppCompatActivity() {

    private lateinit var txtCourse: EditText
    private lateinit var txtTask: EditText
    private lateinit var txtType: EditText
    private lateinit var txtDeadline: EditText
    private lateinit var txtLink: EditText
    private lateinit var btnSave: Button

    private var isEditing = false
    private var taskId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_main_update_task)

        initializeUI()
        checkForEditingTask()

        btnSave.setOnClickListener {
            saveTaskToFirebase()
        }
    }

    // COMPONENTS
    private fun initializeUI() {
        txtCourse = findViewById(R.id.edit_task_course)
        txtTask = findViewById(R.id.edit_task_name)
        txtType = findViewById(R.id.edit_task_type)
        txtDeadline = findViewById(R.id.edit_task_deadline)
        txtLink = findViewById(R.id.edit_task_link)
        btnSave = findViewById(R.id.btn_id_update)
    }

    // FUNCTION FOR CHECK THE TASK IF BEING EDITED AND DISPLAY ITS DETAILS ALSO
    private fun checkForEditingTask() {
        if (intent.hasExtra("TASK_MODEL")) {
            val taskModel = intent.getParcelableExtra<TaskModel>("TASK_MODEL")

            if (taskModel != null) {
                isEditing = true
                taskId = taskModel.taskId
                displayTaskDetails(taskModel)
            } else {
                showToast("ERROR, PLEASE TRY AGAIN.")
            }
        }
    }

    // DISPLAY THE TASK DETAILS IN UI
    private fun displayTaskDetails(taskModel: TaskModel) {
        txtCourse.setText(taskModel.taskCourse)
        txtTask.setText(taskModel.taskName)
        txtType.setText(taskModel.taskType)
        txtDeadline.setText(taskModel.taskDeadline)
        txtLink.setText(taskModel.taskLink)
    }

    // SAVE THE TASK IN DETAILS TO THE FIREBASE (DATABASE)
    private fun saveTaskToFirebase() {
        val course = txtCourse.text.toString().trim()
        val task = txtTask.text.toString().trim()
        val type = txtType.text.toString().trim()
        val deadline = txtDeadline.text.toString().trim()
        val link = txtLink.text.toString().trim()

        val emptyFields = isValidInput(course, task, type, deadline, link)

        if (emptyFields.isEmpty()) {
            if (isEditing) {
                updateTaskInFirebase(course, task, type, deadline, link)
            } else {
                addTaskToFirebase(course, task, type, deadline, link)
            }

            navigateToMainActivity()
        } else {
            val message = "PLEASE FILL UP THE BLANK OF:\n${emptyFields.joinToString(", ")}"
            showToast(message)
        }
    }

    // FUNCTION CHECK IF THE INPUT IS VALID
    private fun isValidInput(course: String, task: String, type: String, deadline: String, link: String): List<String> {
        val emptyFields = mutableListOf<String>()

        if (course.isEmpty()) {
            emptyFields.add("COURSE")
        }

        if (task.isEmpty()) {
            emptyFields.add("TASK")
        }

        if (type.isEmpty()) {
            emptyFields.add("TYPE")
        }

        if (deadline.isEmpty()) {
            emptyFields.add("DEADLINE")
        }

        if (link.isEmpty()) {
            emptyFields.add("LINK")
        }

        return emptyFields
    }

    // FUNCTION FOR ADD A NEW TASK TO FIREBASE (DATABASE)
    private fun addTaskToFirebase(course: String, task: String, type: String, deadline: String, link: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("tasks")
        val taskId = databaseReference.push().key
        val taskModel = TaskModel(taskId, course, task, type, deadline, link)
        databaseReference.child(taskId ?: "").setValue(taskModel)
    }

    // UPDATE THE EXISTING TASK IN FIREBASE (DATABASE)
    private fun updateTaskInFirebase(course: String, task: String, type: String, deadline: String, link: String) {
        taskId?.let { taskId ->
            val taskRef = FirebaseDatabase.getInstance().reference.child("tasks").child(taskId)
            val updatedTaskModel = TaskModel(taskId, course, task, type, deadline, link)
            taskRef.setValue(updatedTaskModel)
        }
    }

    // NAVIGATE OR INTENT BACK TO THE MAIN ACTIVITY TASK
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivityTASK::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        setResult(Activity.RESULT_OK, intent)
        startActivity(intent)
        finish()
    }

    // Display a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
