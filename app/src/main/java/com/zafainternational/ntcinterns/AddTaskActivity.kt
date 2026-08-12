package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var deadlineInput: EditText
    private lateinit var internSpinner: Spinner
    private lateinit var assignButton: Button

    private var internIds =
        ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_add_task
        )

        database =
            DatabaseHelper(this)

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        deadlineInput =
            findViewById(R.id.deadlineInput)

        internSpinner =
            findViewById(R.id.internSpinner)

        assignButton =
            findViewById(R.id.assignButton)

        loadInterns()

        assignButton.setOnClickListener {

            assignTask()
        }
    }

    private fun loadInterns() {

        val interns =
            database.getInterns()

        internIds.clear()

        for (item in interns) {

            val parts =
                item.split(" - ")

            if (parts.size >= 2) {

                internIds.add(
                    parts[0].toInt()
                )
            }
        }

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                interns
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        internSpinner.adapter =
            adapter
    }

    private fun assignTask() {

        val title =
            titleInput.text.toString().trim()

        val description =
            descriptionInput.text.toString().trim()

        val deadline =
            deadlineInput.text.toString().trim()

        if (title.isEmpty()) {

            titleInput.error =
                "Enter task title"

            return
        }

        if (description.isEmpty()) {

            descriptionInput.error =
                "Enter description"

            return
        }

        if (deadline.isEmpty()) {

            deadlineInput.error =
                "Enter deadline"

            return
        }

        if (internIds.isEmpty()) {

            Toast.makeText(
                this,
                "No interns available",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val selectedPosition =
            internSpinner.selectedItemPosition

        val internId =
            internIds[selectedPosition]

        val success =
            database.addTask(
                title,
                description,
                internId,
                deadline,
            )

        if (success) {

            Toast.makeText(
                this,
                "Task assigned successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Failed to assign task",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
