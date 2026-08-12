
package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReviewTaskActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    private lateinit var taskTitleText: TextView
    private lateinit var taskDescriptionText: TextView
    private lateinit var taskDeadlineText: TextView
    private lateinit var taskStatusText: TextView

    private lateinit var feedbackInput: EditText

    private lateinit var approveButton: Button
    private lateinit var rejectButton: Button

    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_review_task
        )

        database =
            DatabaseHelper(this)

        taskId =
            intent.getIntExtra(
                "taskId",
                -1
            )

        taskTitleText =
            findViewById(R.id.taskTitleText)

        taskDescriptionText =
            findViewById(R.id.taskDescriptionText)

        taskDeadlineText =
            findViewById(R.id.taskDeadlineText)

        taskStatusText =
            findViewById(R.id.taskStatusText)

        feedbackInput =
            findViewById(R.id.feedbackInput)

        approveButton =
            findViewById(R.id.approveButton)

        rejectButton =
            findViewById(R.id.rejectButton)

        loadTask()

        approveButton.setOnClickListener {

            approveTask()
        }

        rejectButton.setOnClickListener {

            rejectTask()
        }
    }

    private fun loadTask() {

        if (taskId == -1) {

            Toast.makeText(
                this,
                "Invalid task",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }

        val task =
            database.getTask(taskId)

        if (task == null) {

            Toast.makeText(
                this,
                "Task not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }

        taskTitleText.text =
            task[0]

        taskDescriptionText.text =
            "Description:\n${task[1]}"

        taskDeadlineText.text =
            "Deadline: ${task[2]}"

        taskStatusText.text =
            "Status: ${task[3]}"

        feedbackInput.setText(
            task[4]
        )
    }

    private fun approveTask() {

        val feedback =
            feedbackInput.text.toString().trim()

        val success =
            database.approveTask(
                taskId,
                feedback
            )

        if (success) {

            Toast.makeText(
                this,
                "Task approved",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Could not approve task",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun rejectTask() {

        val feedback =
            feedbackInput.text.toString().trim()

        if (feedback.isEmpty()) {

            feedbackInput.error =
                "Enter rejection feedback"

            return
        }

        val success =
            database.rejectTask(
                taskId,
                feedback
            )

        if (success) {

            Toast.makeText(
                this,
                "Task rejected",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Could not reject task",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
