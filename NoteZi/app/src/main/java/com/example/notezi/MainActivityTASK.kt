package com.example.notezi

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class MainActivityTASK : AppCompatActivity() {

    private lateinit var mainTasksRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var addBtn: ImageView
    private lateinit var taskCountTextView: TextView
    private lateinit var searchView: SearchView

    companion object {
        const val ADD_TASK_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task)

        // SET UP FOR EVENT LISTENERS
        initializeUI()
        // RECYCLERVIEW FOR DISPLAYING THE TASK
        initializeRecyclerView()
        // SET LISTENERS FOR NAVIGATION ON BTN AND UPDATE TASK COUNT
        setButtonListeners()
        // UPDATING THE INITIAL TASK COUNT
        updateTaskCount()
    }

    private fun initializeUI() {
        addBtn = findViewById(R.id.add_btn)
        taskCountTextView = findViewById(R.id.taskId_txt)
        searchView = findViewById(R.id.searchTask_btn)

        // ADD BUTTON TO START CREATE TASK
        addBtn.setOnClickListener {
            startUpdateTaskActivity()
        }

        // SEARCH VIEW FOR SEARCH FUNCTION AND QUERY
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // FUNCTION FOR USER IF TYPES IN SEARCH VIEW
                performSearch(newText.orEmpty())
                return true
            }
        })
    }

    private fun initializeRecyclerView() {
        mainTasksRecyclerView = findViewById(R.id.mainTasks_id)
        mainTasksRecyclerView.layoutManager = LinearLayoutManager(this)

        // FIREBASE RECYCLER ADAPTER TO POPULATE THE RECYCLER VIEW WITH TASKS FROM DATABASE
        val options = FirebaseRecyclerOptions.Builder<TaskModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("tasks"), TaskModel::class.java)
            .build()

        taskAdapter = TaskAdapter(options)
        mainTasksRecyclerView.adapter = taskAdapter

        // FUNCTION FOR LONG PRESS
        taskAdapter.setOnItemClickListener { taskModel ->
            showLinkConfirmationDialog(taskModel)
        }

        // FUNCTION FOR ONE CLICK
        taskAdapter.setOnItemLongPressListener { taskModel ->
            showLongPressDialog(taskModel)
        }
    }

    private fun setButtonListeners() {
        // FUNCTION FOR NAVIGATE TO INTENT ANOTHER ACTIVITIES
        findViewById<View>(R.id.task_btn).setOnClickListener {
            startMainActivity(MainActivityTASK::class.java)
        }

        findViewById<View>(R.id.sched_btn).setOnClickListener {
            startMainActivity(MainActivitySCHEDULE::class.java)
        }

        findViewById<View>(R.id.profile_btn).setOnClickListener {
            startMainActivity(MainActivityPROFILE::class.java)
        }
    }

    private fun startMainActivity(activityClass: Class<*>) {
        // START ACTIVITY SPECIFIED
        val myIntent = Intent(this, activityClass)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(myIntent)
    }

    private fun startUpdateTaskActivity() {
        // FUNCTION TO START THE TASK CREATE AND ALSO UPDATING
        val myIntent = Intent(this, SideMainUPDATETASK::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        startActivityForResult(myIntent, ADD_TASK_REQUEST)
    }

    private fun showLinkConfirmationDialog(taskModel: TaskModel) {
        // SHOW THE ALERT DIALOG FOR CONFIRMING THE OPENING LINK
        val link = taskModel.taskLink

        AlertDialog.Builder(this)
            .setTitle("GO TO LINK?")
            .setMessage("DO YOU WANT TO GO TO THE LINK?")
            .setPositiveButton("YES") { _, _ ->
                if (link.isNotBlank()) {
                    // SHOW ALERT DIALOG TO CHOOSE WHAT APP FOR OPENING THE LINK
                    showAppChooserDialog(link)
                }
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAppChooserDialog(link: String) {
        // SHOW ALERT DIALOG TO CHOOSE WHAT APP FOR OPENING THE LINK
        AlertDialog.Builder(this)
            .setTitle("OPEN THE LINK WITH?")
            .setItems(arrayOf("GOOGLE MEET", "GOOGLE CHROME")) { _, which ->
                when (which) {
                    0 -> openLinkInGoogleMeet(link)
                    1 -> openLinkInApp(link, "com.android.chrome")
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openLinkInApp(link: String, packageName: String) {
        // OPEN THE LINK ON SPECIFIED APP OR TOAST THE MESSAGE IF THE APP IS NOT INSTALLED
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.`package` = packageName

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showToast()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openLinkInGoogleMeet(link: String) {
        // OPEN THE LINK IN GOOGLE MEET OR SHOW A TOAST MESSAGE IF THE APP IS NOT INSTALLED
        val googleMeetPackageName = "com.google.android.apps.meetings"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.`package` = googleMeetPackageName

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showToast()
        }
    }

    private fun showToast() {
        // Display a short toast message
        Toast.makeText(this, "PLEASE INSTALL GOOGLE MEET TO PROCEED WITH THE LINK", Toast.LENGTH_SHORT).show()
    }

    private fun showLongPressDialog(taskModel: TaskModel) {
        // FUNCTION LONG PRESS SHOW ALERT DIALOG FOR OPTION, EDIT, DELETE.
        val options = arrayOf("EDIT", "DELETE")

        AlertDialog.Builder(this)
            .setTitle("CHOOSE IF WANT TO DELETE, OR EDIT THE TASK:")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editTask(taskModel)
                    1 -> showDeleteConfirmationDialog(taskModel)
                }
            }
            .show()
    }

    // Function to show the delete confirmation dialog
    private fun showDeleteConfirmationDialog(taskModel: TaskModel) {
        // Creates a dialog box
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete this task?")
            // If YES, then it will remove it from Firebase and update task counter
            .setPositiveButton("Yes") { _, _ ->
                deleteTask(taskModel)
                updateTaskCount()
            }
            // If NO, then it will close the dialog box
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    private fun editTask(taskModel: TaskModel) {
        // START THE TASK EDITING ACTIVITY WITH THE SELECTED USER TASK
        val intent = Intent(this, SideMainUPDATETASK::class.java)
        intent.putExtra("TASK_MODEL", taskModel)
        startActivityForResult(intent, ADD_TASK_REQUEST)
    }

    // Function for delete the task from the database also
    private fun deleteTask(taskModel: TaskModel) {
        // DELETE THE SELECTED TASK FROM THE FIREBASE AND UPDATE ALSO IN THE TASK COUNTER
        taskModel.taskId?.let {
            val taskRef = FirebaseDatabase.getInstance().reference.child("tasks").child(it)
            taskRef.removeValue()
            // For something in updating the task count
        }
    }

    private fun updateTaskCount() {
        // UPDATE THE TASK COUNT BASED ON THE USER CREATED
        val taskReference = FirebaseDatabase.getInstance().reference.child("tasks")

        taskReference.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                taskCountTextView.text = "Task Count: $count"
            }

            override fun onCancelled(error: DatabaseError) {
                // HANDLE ERROR IF NEEDED
            }
        })
    }

    private fun performSearch(searchTerm: String) {
        // SEARCH VIEW PERFORM BASED ON THE ENTERED QUERY AND UPDATE ON RECYCLER VIEW ACCORDINGLY
        taskAdapter.stopListening()

        val query = if (searchTerm.isNotEmpty()) {
            FirebaseDatabase.getInstance().reference.child("tasks")
                .orderByChild("taskCourse").startAt(searchTerm).endAt(searchTerm + "\uf8ff")
        } else {
            FirebaseDatabase.getInstance().reference.child("tasks")
        }

        val newOptions = FirebaseRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java)
            .build()

        val newTaskAdapter = TaskAdapter(newOptions)
        newTaskAdapter.setOnItemClickListener { taskModel ->
            showLinkConfirmationDialog(taskModel)
        }

        newTaskAdapter.setOnItemLongPressListener { taskModel ->
            showLongPressDialog(taskModel)
        }

        newTaskAdapter.startListening()
        mainTasksRecyclerView.adapter = newTaskAdapter

        // TASK COUNT UPDATE FOR NEW ADAPTER
        updateTaskCount()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // HANDLE FOR THE RESULT FROM THE CREATE OR UPDATING ACTIVITY
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            showToast()
            taskAdapter.stopListening()
            taskAdapter.startListening()
            updateTaskCount()
        }
    }

    override fun onStart() {
        // START LISTENING FOR CHANGES ON FIREBASE RECYCLER ADAPTER IF THE ACTIVITY STARTS
        super.onStart()
        taskAdapter.startListening()
    }

    override fun onStop() {
        // STOP LISTENING FOR CHANGES ON FIREBASE RECYCLER ADAPTER IF THE ACTIVITY STOPS
        super.onStop()
        taskAdapter.stopListening()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // FINISH THE ACTIVITY WHEN THE BACK BUTTON IS PRESSED
        finish()
        super.onBackPressed()
    }
}
