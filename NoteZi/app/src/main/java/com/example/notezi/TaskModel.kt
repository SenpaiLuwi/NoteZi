package com.example.notezi

import android.os.Parcel
import android.os.Parcelable

// THE DATA CLASS FOR REPRESENTING THE TASK, PASSING THE DATA BETWEEN ACTIVITIES.
data class TaskModel(
    var taskId: String? = null,
    val taskCourse: String = "",
    val taskName: String = "",
    val taskType: String = "",
    val taskDeadline: String = "",
    val taskLink: String = ""
) : Parcelable {

    // SECONDARY CONSTRUCTOR FOR CREATING EMPTY TASK MODEL
    constructor() : this(null, "", "", "", "", "")

    // SECONDARY CONSTRUCTOR FOR CREATING EMPTY TASK MODEL FROM PARCEL
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // WRITE THE OBJECT DATA TO PARCEL
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskCourse)
        parcel.writeString(taskName)
        parcel.writeString(taskType)
        parcel.writeString(taskDeadline)
        parcel.writeString(taskLink)
    }

    // DESCRIBE THE KINDS OF SPECIAL OBJECTS CONTAINED IN THE PARCELABLE INSTANCE
    override fun describeContents(): Int {
        return 0
    }


    // COMPANION OBJECT IMPLEMENTING PARCELABLE.
    companion object CREATOR : Parcelable.Creator<TaskModel> {
        // Create a TaskModel instance from a Parcel
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }


        // CREATE THE ARRAY OF TASK MODEL INSTANCE OF THE SPECIFIED SIZE
        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }
}
