package com.example.notezi

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class MainActivityTASK : AppCompatActivity() {

    // VARIABLES
    private lateinit var mainTasksRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var addBtn: ImageView
    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View

    private lateinit var taskCountTextView: TextView

    companion object {
        const val ADD_TASK_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task)

        // UI COMPONENTS
        initializeUI()

        // RECYCLERVIEW AND ALSO THE ADAPTER
        initializeRecyclerView()

        // CLICK LISTENERS FOR BUTTONS
        setButtonListeners()

        // UPDATE TASK COUNTER
        updateTaskCount()
    }

    // FUNCTION FOR UI COMPONENTS PAR
    private fun initializeUI() {
        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)
        taskCountTextView = findViewById(R.id.taskId_txt)
        addBtn = findViewById(R.id.add_btn)
    }

    // FUNCTION FOR RECYCLERVIEW & ADAPTER
    private fun initializeRecyclerView() {
        mainTasksRecyclerView = findViewById(R.id.mainTasks_id)
        mainTasksRecyclerView.layoutManager = LinearLayoutManager(this)

        val options = FirebaseRecyclerOptions.Builder<TaskModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("tasks"), TaskModel::class.java)
            .build()

        // SET CLICKER FOR ITEM AND LONG PRESS ALSO

        taskAdapter = TaskAdapter(options)
        mainTasksRecyclerView.adapter = taskAdapter

        taskAdapter.setOnItemClickListener { taskModel ->
            showOptionsDialog(taskModel)
        }

        taskAdapter.setOnItemLongPressListener { taskModel ->
            showLongPressDialog(taskModel)
        }
    }

    // FUNCTION FOR BUTTONS SET CLICKER ETC.
    private fun setButtonListeners() {
        addBtn.setOnClickListener {
            startUpdateTaskActivity()
        }

        taskBtn.setOnClickListener {
            startMainActivity(MainActivityTASK::class.java)
        }

        schedBtn.setOnClickListener {
            startMainActivity(MainActivitySCHEDULE::class.java)
        }

        profileBtn.setOnClickListener {
            startMainActivity(MainActivityPROFILE::class.java)
        }
    }

    // FUNCTION FOR START NEW ACT BASED ON THE PROVIDED THE CLASS
    private fun startMainActivity(activityClass: Class<*>) {
        val myIntent = Intent(this, activityClass)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(myIntent)
    }

    // FUNCTION START THE SideMainUPDATETASK.kt
    private fun startUpdateTaskActivity() {
        val myIntent = Intent(this, SideMainUPDATETASK::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivityForResult(myIntent, ADD_TASK_REQUEST)
    }

    // FUNCTION FOR SHOW ALERT DIALOG WITH OPTION
    // EDIT / DELETE / OPEN LINK
    private fun showOptionsDialog(taskModel: TaskModel) {
        val options = arrayOf("Edit", "Delete", "Open Link")

        AlertDialog.Builder(this)
            .setTitle("Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editTask(taskModel)
                    1 -> deleteTask(taskModel)
                    2 -> openLink(taskModel)
                }
            }
            .show()
    }

    // FUNCTION FOR OPEN LINK
    private fun openLink(taskModel: TaskModel) {
        val link = taskModel.taskLink

        if (link.isNotEmpty()) {
            val intent = createLinkIntent(link)
            startActivity(intent)
        } else {
            showToast("Task does not have a valid link.")
        }
    }

    private fun createLinkIntent(link: String): Intent {
        return if (link.startsWith("http://") || link.startsWith("https://")) {
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        } else {
            Intent(Intent.ACTION_VIEW, Uri.parse("http://$link"))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // FUNCTION FOR SHOW DIALOG OPTION FOR LONG PRESS CLICK
    private fun showLongPressDialog(taskModel: TaskModel) {
        val options = arrayOf("Edit", "Delete")

        AlertDialog.Builder(this)
            .setTitle("Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editTask(taskModel)
                    1 -> deleteTask(taskModel)
                }
            }
            .show()
    }

    // FUNCTION FOR EDIT DIALOG IF USER WANT TO EDIT TASK
    private fun editTask(taskModel: TaskModel) {
        val intent = Intent(this, SideMainUPDATETASK::class.java)
        intent.putExtra("TASK_MODEL", taskModel)
        startActivityForResult(intent, ADD_TASK_REQUEST)
    }

    // FUNCTION FOR DELETED TASK
    private fun deleteTask(taskModel: TaskModel) {
        val taskId = taskModel.taskId

       // REMOVE THE TASK FROM THE DATABASE:
        taskId?.let {
            val taskRef = FirebaseDatabase.getInstance().reference.child("tasks").child(it)
            taskRef.removeValue()

            // FUNCTION TASK COUNT AFTER DELETION // DEBUG THIS: MAKE TASK COUNT AFTER CREATE TASK
            updateTaskCount()
        }
    }

    // FUNCTION TO UPDATE COUNT THE TASK AND DISPLAYED
    @SuppressLint("SetTextI18n")
    private fun updateTaskCount() {

        // FETCH TO CURRENT TASK COUNT FROM THE FIREBASE DATABASE OR ADAPTERS
        val currentTaskCount = taskAdapter.itemCount

        // UPDATE TEXTVIEW WITH CURRENT TASK COUNT
        taskCountTextView.text = "Task Count: $currentTaskCount"
    }

    override fun onStart() {
        super.onStart()
        taskAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        taskAdapter.stopListening()
    }
}
