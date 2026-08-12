package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    private var taskId = -1

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var deadlineText: TextView
    private lateinit var statusText: TextView

    private lateinit var startButton: Button
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_task_detail,
        )

        database =
            DatabaseHelper(this)

        taskId =
            intent.getIntExtra(
                "taskId",
                -1
            )

        titleText =
            findViewById(R.id.titleText)

        descriptionText =
            findViewById(R.id.descriptionText)

        deadlineText =
            findViewById(R.id.deadlineText)

        statusText =
            findViewById(R.id.statusText)

        startButton =
            findViewById(R.id.startButton)

        submitButton =
            findViewById(R.id.submitButton)

        loadTask()

        startButton.setOnClickListener {

            val success =
                database.startTask(
                    taskId
                )

            if (success) {

                Toast.makeText(
                    this,
                    getString(R.string.msg_task_started),
                    Toast.LENGTH_SHORT
                ).show()

                loadTask()
            }
        }

        submitButton.setOnClickListener {

            val success =
                database.submitTask(
                    taskId
                )

            if (success) {

                Toast.makeText(
                    this,
                    getString(R.string.msg_task_submitted),
                    Toast.LENGTH_LONG
                ).show()

                loadTask()
            }
        }
    }

    private fun loadTask() {

        val task =
            database.getTask(taskId)

        if (task != null) {

            titleText.text =
                getString(R.string.label_title, task[0])

            descriptionText.text =
                getString(R.string.label_description, task[1])

            deadlineText.text =
                getString(R.string.label_deadline, task[2])

            statusText.text =
                getString(R.string.label_status, task[3])

            when (task[3]) {

                "Assigned" -> {

                    startButton.isEnabled =
                        true

                    submitButton.isEnabled =
                        false
                }

                "In Progress" -> {

                    startButton.isEnabled =
                        false

                    submitButton.isEnabled =
                        true
                }

                "Completed",
                "Approved",
                "Rejected" -> {

                    startButton.isEnabled =
                        false

                    submitButton.isEnabled =
                        false
                }
            }
        }
    }
}