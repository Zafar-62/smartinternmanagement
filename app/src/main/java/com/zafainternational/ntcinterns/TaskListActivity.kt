package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val taskList = findViewById<LinearLayout>(
            R.id.taskList
        )

        addTask(
            taskList,
            "Prepare Internship Report",
            "Prepare the weekly internship report.",
            "10 August 2026",
            "Pending"
        )

        addTask(
            taskList,
            "Complete Android Module",
            "Complete the assigned Android development module.",
            "15 August 2026",
            "In Progress"
        )
    }

    private fun addTask(
        parent: LinearLayout,
        title: String,
        description: String,
        deadline: String,
        status: String
    ) {

        val taskText = TextView(this)

        taskText.text =
            "Task: $title\n\n"+"Description: $description\n\n"+"Deadline: $deadline\n\n"+"Status: $status"

        taskText.textSize = 17f

        taskText.setPadding(
            20,
            20,
            20,
            20
        )

        parent.addView(taskText)

        val openButton = Button(this)

        openButton.text = "Open Task"

        openButton.setOnClickListener {

            Toast.makeText(
                this,
                "Task: $title",
                Toast.LENGTH_SHORT
            ).show()
        }

        parent.addView(openButton)
    }
}