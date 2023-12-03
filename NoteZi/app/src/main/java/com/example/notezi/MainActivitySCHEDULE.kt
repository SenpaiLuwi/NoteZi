package com.example.notezi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import java.util.*
import android.widget.Toast

@Suppress("NAME_SHADOWING")
class MainActivitySCHEDULE : AppCompatActivity() {

    // Views, TextViews, RecyclerView, ImageButton, SearchView, and ScheduleAdapter
    private lateinit var taskBtn: View
    private lateinit var schedBtn: View
    private lateinit var profileBtn: View
    private lateinit var timeView: TextView
    private lateinit var dayDate: TextView
    private lateinit var mainSched: RecyclerView
    private lateinit var addButton: ImageButton
    private lateinit var searchSched: SearchView
    private lateinit var mainAdapter: ScheduleAdapter

    // START OF ONCREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_schedule)

        // INITIALIZE OF THE FUNCTIONS

        taskBtn = findViewById(R.id.task_btn)
        schedBtn = findViewById(R.id.sched_btn)
        profileBtn = findViewById(R.id.profile_btn)
        timeView = findViewById(R.id.time_id)
        dayDate = findViewById(R.id.dayName_id)
        mainSched = findViewById(R.id.mainSched_id)
        addButton = findViewById(R.id.add_btn)
        searchSched = findViewById(R.id.searchSched_btn)

        // LINEARLAYOUT MANAGER
        mainSched.layoutManager = LinearLayoutManager(this)

        // FIREBASE RECYCLER OPTIONS PARAMETERED BY "ScheduleModel". SetQuery Meaning Fetching Data to the Database from "schedule".
        // ScheduleModel is the Adapter and the Getters and Setters for the Code.
        val options: FirebaseRecyclerOptions<ScheduleModel> =
            FirebaseRecyclerOptions.Builder<ScheduleModel>()
                .setQuery(FirebaseDatabase.getInstance().reference.child("schedule"), ScheduleModel::class.java)
                .build()

        // Getting the CurrentDate of the users Phone
        val currentDate = getCurrentDate()
        timeView.text = currentDate

        // Getting the CurrentDay of the users Phone
        val currentDay = getCurrentDay()
        dayDate.text = currentDay

        // Instance of the ScheduleAdapter "options" as the Parameter
        mainAdapter = ScheduleAdapter(options)
        // Associates the created ScheduleAdapter
        mainSched.adapter = mainAdapter
        // Initiates the listening process for the FirebaseRecyclerAdapter.
        mainAdapter.startListening()

        // Add Button on the Top Right of the Screen
        addButton.setOnClickListener {
            Toast.makeText(this, "Add A Schedule", Toast.LENGTH_SHORT).show()
            // Calling the showUpdateDialog
            showUpdateDialog()
        }

        // Task Button
        taskBtn.setOnClickListener {
            Toast.makeText(this, "Task", Toast.LENGTH_SHORT).show()
            val myIntent = Intent(this, MainActivityTASK::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        // Schedule Button
        schedBtn.setOnClickListener {
            Toast.makeText(this, "You are already in the Schedule Section", Toast.LENGTH_SHORT).show()
            // Refreshes the CurrentDate of the users Phone
            val currentDate = getCurrentDate()
            timeView.text = currentDate
            // Refreshes the CurrentDay of the users Phone
            val currentDay = getCurrentDay()
            dayDate.text = currentDay

            val myIntent = Intent(this, MainActivitySCHEDULE::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        // Profile Button
        profileBtn.setOnClickListener {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            val myIntent = Intent(this, MainActivityPROFILE::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(myIntent)
        }

        // ON CLICK LISTENER FOR CARD VIEWS
        mainSched.addOnItemTouchListener(
            RecyclerItemClickListener(this, mainSched, object : RecyclerItemClickListener.OnItemClickListener {
                //If the user CLICKS the Card View
                override fun onItemClick(view: View?, position: Int) {
                    // CALLS THE showLinkConfirmationDialog
                    showLinkConfirmationDialog(position)
                }

                // If the user CLICK AND HOLD the Card View
                override fun onItemLongClick(view: View?, position: Int) {
                    // CALLS THE showLongClickDialog
                    showLongClickDialog(position)
                }
            })
        )

        // SEARCH FUNCTIONALITY
        searchSched.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                txtSearch(newText)
                return true
            }
        })
    }

    // START OF showUpdateDialog FUNCTION
    private fun showUpdateDialog() {
        // DIALOG PLUS POP UP
        val dialogPlus = DialogPlus.newDialog(this)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(R.layout.side_main_update_schedule))
            .setExpanded(true, 1150)
            .create()

        // INITIALIZE OF THE FUNCTIONS
        val updateView = dialogPlus.holderView
        val editSubjName: EditText = updateView.findViewById(R.id.edit_subj_name)
        val editSubjProf: EditText = updateView.findViewById(R.id.edit_subj_prof)
        val editSubjTime: EditText = updateView.findViewById(R.id.edit_subj_time)
        val editSubjDay: EditText = updateView.findViewById(R.id.edit_subj_day)
        val editSubjLink: EditText = updateView.findViewById(R.id.edit_subj_link)
        val btnUpdate: Button = updateView.findViewById(R.id.btn_id_update)
        btnUpdate.setOnClickListener {
            val subjName = editSubjName.text.toString()
            val subjProf = editSubjProf.text.toString()
            val subjTime = editSubjTime.text.toString()
            val subjDay = editSubjDay.text.toString()
            val subjLink = editSubjLink.text.toString()

            // VALIDATION OF THE USER INPUT
            if (subjName.isEmpty()) {
                validateMsg("Course Name", this)
            } else if (subjProf.isEmpty()) {
                validateMsg("Course Professor", this)
            } else if (subjTime.isEmpty()) {
                validateMsg("Course Time", this)
            } else if (subjDay.isEmpty()) {
                validateMsg("Course Day", this)
            } else if (subjLink.isEmpty()) {
                validateMsg("Course Link", this)
            } else {
                Toast.makeText(this, "Schedule Added", Toast.LENGTH_SHORT).show()
                // SAVING IT TO THE DATABASE
                val scheduleRef = FirebaseDatabase.getInstance().reference.child("schedule").push()
                val scheduleModel = ScheduleModel(subjName, subjProf, subjTime, subjDay, subjLink)
                scheduleRef.setValue(scheduleModel)

                dialogPlus.dismiss()
            }
        }
        dialogPlus.show()
    }

    // START OF showEditDialog FUNCTION "THIS ONLY OCCURS WHEN THE USER CLICK AND HOLD THE CARD VIEW"
    private fun showEditDialog(position: Int) {
        // DIALOG PLUS POP UP
        val dialogPlus = DialogPlus.newDialog(this)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(R.layout.side_main_update_schedule))
            .setExpanded(true, 1100)
            .create()

        // INITIALIZE OF THE FUNCTIONS
        val updateView = dialogPlus.holderView
        val editSubjName: EditText = updateView.findViewById(R.id.edit_subj_name)
        val editSubjProf: EditText = updateView.findViewById(R.id.edit_subj_prof)
        val editSubjTime: EditText = updateView.findViewById(R.id.edit_subj_time)
        val editSubjDay: EditText = updateView.findViewById(R.id.edit_subj_day)
        val editSubjLink: EditText = updateView.findViewById(R.id.edit_subj_link)
        val btnUpdate: Button = updateView.findViewById(R.id.btn_id_update)

        // RETRIEVE THE CURRENT DATA
        val currentItem = mainAdapter.getItem(position)

        // SET THE CURRENT VALUES TO THE EDIT TEXT FIELDS
        editSubjName.setText(currentItem.subjName)
        editSubjProf.setText(currentItem.subjProf)
        editSubjTime.setText(currentItem.subjTime)
        editSubjDay.setText(currentItem.subjDay)
        editSubjLink.setText(currentItem.subjLink)

        // BUTTON UPDATE INSIDE THE DIALOG PLUS
        btnUpdate.setOnClickListener {
            // GET THE UPDATED VALUES FROM THE EDIT TEXT FIELDS
            val updatedName = editSubjName.text.toString()
            val updatedProf = editSubjProf.text.toString()
            val updatedTime = editSubjTime.text.toString()
            val updatedDay = editSubjDay.text.toString()
            val updatedLink = editSubjLink.text.toString()

            // VALIDATION OF THE USER INPUT
            if (updatedName.isEmpty()) {
                validateMsg("Course Name", this)
            } else if (updatedProf.isEmpty()) {
                validateMsg("Course Professor", this)
            } else if (updatedTime.isEmpty()) {
                validateMsg("Course Time", this)
            } else if (updatedDay.isEmpty()) {
                validateMsg("Course Day", this)
            } else if (updatedLink.isEmpty()) {
                validateMsg("Course Link", this)
            } else {
                Toast.makeText(this, "Schedule Updated", Toast.LENGTH_SHORT).show()
                // UPDATE THE ITEM IN THE DATABASE
                updateItem(position, updatedName, updatedProf, updatedTime, updatedDay, updatedLink)
                dialogPlus.dismiss()
            }
        }
        dialogPlus.show()
    }

    // START OF THE UPDATING THE ITEMS
    private fun updateItem(position: Int, newName: String, newProf: String, newTime: String, newDay: String, newLink: String) {
        // THIS VARIABLE HOLDS THE POSITION OF THE ITEM
        val itemId = mainAdapter.getRef(position).key

        // IF THE ITEMS INSIDE THE ITEMID IS NOT NULL THEN IT WILL SAVE TO THE DATABASE
        if (itemId != null) {
            val updatedItem = ScheduleModel(newName, newProf, newTime, newDay, newLink)
            FirebaseDatabase.getInstance().reference.child("schedule").child(itemId).setValue(updatedItem)
        }
    }

    // START OF showDeleteConfirmationDialog FUNCTION
    private fun showDeleteConfirmationDialog(position: Int) {
        // CREATES A DIALOG BOX
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete this subject?")
            // IF YES THEN IT WILL REMOVE IT TO THE DATABASE
            .setPositiveButton("Yes") { _, _ ->
                // CALLING THE deleteItem
                deleteItem(position)
            }
            // IF NO THEN IT WILL CLOSE THE DIALOG BOX
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    // START OF THE deleteItem WITH THE HELP OF removeValue()
    private fun deleteItem(position: Int) {
        val itemId = mainAdapter.getRef(position).key
        if (itemId != null) {
            Toast.makeText(this, "Schedule Deleted", Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().reference.child("schedule").child(itemId).removeValue()
        }
    }

    // START OF showLongClickDialog "THIS WILL ONLY OCCUR IF THE USER CLICK AND HOLD THE CARD VIEW"
    private fun showLongClickDialog(position: Int) {
        val longClickOptions = arrayOf("Edit", "Delete")

        // ALERT DIALOG POP UP
        AlertDialog.Builder(this)
            .setTitle("What do you want to do in this subject?")
            .setItems(longClickOptions) { _, which ->
                when (which) {
                    0 -> showEditDialog(position) // CALLING THE showEditDialog
                    1 -> showDeleteConfirmationDialog(position) // CALLING THE showDeleteConfirmationDialog
                }
            }
            .show()
    }

    // START OF GETTING THE CURRENT DATE ON LOCAL PHONE
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }

    // START OF showLinkConfirmationDialog "THIS ONLY OCCURS WHEN THE CARD VIEW IS CLICKED"
    private fun showLinkConfirmationDialog(position: Int) {
        val currentItem = mainAdapter.getItem(position)
        val link = currentItem.subjLink

        // ALERT DIALOG
        AlertDialog.Builder(this)
            .setTitle("Go to Link")
            .setMessage("Do you want to go to the link?")
            // IF THE USER CLICKS YES
            .setPositiveButton("Yes") { _, _ ->
                if (link.isNotBlank()) {
                    // CALLING OF showAppChooserDialog
                    showAppChooserDialog(link)
                }
            }
            // IF THE USER CLICKS NO CLOSES THE DIALOG BOX
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // START OF showAppChooserDialog THIS WILL ASK THE USER ON WHAT APP THAT THEY WANT TO USE
    private fun showAppChooserDialog(link: String) {
        val appChooserDialog = AlertDialog.Builder(this)
            .setTitle("Open link with")
            .setItems(arrayOf("Google Meet", "Chrome")) { _, which ->
                when (which) {
                    0 -> openLinkInGoogleMeet(link) // OPENS openLinkInGoogleMeet
                    1 -> openLinkInApp(link, "com.android.chrome") // OPENS openLinkInApp OR GOOGLE CHROME
                }
            }
            // CLOSES THE DIALOG BOX
            .setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
        appChooserDialog.show()
    }

    // START OF openLinkInApp THIS WILL OPEN THE GOOGLE CHROME
    @SuppressLint("QueryPermissionsNeeded")
    private fun openLinkInApp(link: String, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.`package` = packageName

        // IF THE intent IS NOT NULL
        if (intent.resolveActivity(packageManager) != null) {
            Toast.makeText(this, "Opening Chrome", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            // IF THE APP IS NOT INSTALLED
        } else {
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
        }
    }

    // START OF openLinkInGoogleMeet THIS WILL OPEN THE GOOGLE MEET APP
    @SuppressLint("QueryPermissionsNeeded")
    private fun openLinkInGoogleMeet(link: String) {
        // USE THE GOOGLE MEET PACKAGE NAME
        val googleMeetPackageName = "com.google.android.apps.meetings"

        // CREATE AN INTENT WITH THE GOOGLE MEET PACKAGE NAME
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.`package` = googleMeetPackageName
        // IF THE intent IS NOT NULL
        if (intent.resolveActivity(packageManager) != null) {
            Toast.makeText(this, "Opening Google Meet", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            // IF THE APP IS NOT INSTALLED
        } else {
            Toast.makeText(this, "Google Meet app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    // START OF SEARCHING THE ITEMS ON THE LIST
    private fun txtSearch(str:String?){
        val options: FirebaseRecyclerOptions<ScheduleModel> = FirebaseRecyclerOptions.Builder<ScheduleModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("schedule").orderByChild("subjName").startAt(str).endAt("$str~"), ScheduleModel::class.java)
            .build()

        mainAdapter = ScheduleAdapter(options)
        mainAdapter.startListening()
        mainSched.adapter = mainAdapter
    }


    // START OF GETTING THE CURRENT DAY ON LOCAL PHONE
    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()

        // CONVERT DAY OF THE WEEK TO A READABLE FORMAT
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    // VALIDATION OF THE USER INPUT
    private fun validateMsg(field: String, context: Context) {
        Toast.makeText(context, "Please Enter the $field", Toast.LENGTH_SHORT).show()
    }
}