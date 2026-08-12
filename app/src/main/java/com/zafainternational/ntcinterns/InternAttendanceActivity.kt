package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class InternAttendanceActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_intern_attendance
        )

        database =
            DatabaseHelper(this)

        val internId =
            intent.getIntExtra(
                "internId",
                -1
            )

        if (internId == -1) {

            Toast.makeText(
                this,
                "Intern ID not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }

        val percentage =
            database.getAttendancePercentage(
                internId
            )

        findViewById<TextView>(
            R.id.percentageText
        ).text =
            getString(R.string.attendance_percentage, percentage)

        val attendance =
            database.getInternAttendance(
                internId
            )

        findViewById<ListView>(
            R.id.attendanceList
        ).adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                attendance
            )
    }
}
