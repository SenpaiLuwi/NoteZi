package com.example.notezi

import android.os.Parcel
import android.os.Parcelable

// CLASS FOR TASK MODEL AND ALSO WITH PARCELABLE IMPLEMENTATION:
data class TaskModel(
    val taskId: String? = null,
    val taskCourse: String = "",
    val taskName: String = "",
    val taskDeadline: String = "",
    val taskLink: String = ""

    // CONSTRUCTORS:
) : Parcelable {
    constructor() : this(null, "", "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    //PARCELABLE IMPLEMENTATION, TO WRITE OBJECT DATA TO PARCEL:
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskId)
        parcel.writeString(taskCourse)
        parcel.writeString(taskName)
        parcel.writeString(taskDeadline)
        parcel.writeString(taskLink)
    }

    // DESCRIBE THE OBJECT OR IF "SPECIAL" CONTAINS ON PARCELABLE
    override fun describeContents(): Int {
        return 0
    }

    // CREATOR, PARCELABLE COMPANION OBJECT:
    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        // ARRAY OF PARCELABLE GIVEN THE SIZE:
        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }
}
