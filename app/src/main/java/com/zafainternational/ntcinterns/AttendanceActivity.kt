
package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    private lateinit var internSpinner: Spinner
    private lateinit var statusSpinner: Spinner
    private lateinit var dateInput: EditText
    private lateinit var markButton: Button

    private var internIds =
        ArrayList<Int>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_attendance
        )

        database =
            DatabaseHelper(this)

        internSpinner =
            findViewById(
                R.id.internSpinner
            )

        statusSpinner =
            findViewById(
                R.id.statusSpinner
            )

        dateInput =
            findViewById(
                R.id.dateInput
            )

        markButton =
            findViewById(
                R.id.markButton
            )

        val today =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

        dateInput.setText(today)

        loadInterns()

        val statuses =
            arrayOf(
                "Present",
                "Absent",
                "Late",
                "Leave"
            )

        val statusAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                statuses
            )

        statusAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        statusSpinner.adapter =
            statusAdapter

        markButton.setOnClickListener {

            if (internIds.isEmpty()) {
                Toast.makeText(
                    this,
                    "No interns found",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val position =
                internSpinner.selectedItemPosition

            val internId =
                internIds[position]

            val date =
                dateInput.text
                    .toString()
                    .trim()

            val status =
                statusSpinner.selectedItem
                    .toString()

            val success =
                database.markAttendance(
                    internId,
                    date,
                    status
                )

            if (success) {

                Toast.makeText(
                    this,
                    "Attendance marked",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadInterns() {

        val interns =
            database.getInterns()

        val names =
            ArrayList<String>()

        internIds.clear()

        for (item in interns) {

            val parts =
                item.split(" - ")

            if (parts.size >= 2) {

                internIds.add(
                    parts[0].toInt()
                )

                names.add(
                    parts[1]
                )
            }
        }

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                names
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        internSpinner.adapter =
            adapter
    }
}
