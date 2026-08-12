package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SupervisorActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    private lateinit var totalInternsText: TextView
    private lateinit var totalTasksText: TextView
    private lateinit var pendingTasksText: TextView
    private lateinit var completedTasksText: TextView
    private lateinit var approvedTasksText: TextView
    private lateinit var rejectedTasksText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_supervisor
        )

        database =
            DatabaseHelper(this)

        totalInternsText =
            findViewById(R.id.totalInternsText)

        totalTasksText =
            findViewById(R.id.totalTasksText)

        pendingTasksText =
            findViewById(R.id.pendingTasksText)

        completedTasksText =
            findViewById(R.id.completedTasksText)

        approvedTasksText =
            findViewById(R.id.approvedTasksText)

        rejectedTasksText =
            findViewById(R.id.rejectedTasksText)

        val manageInternsButton =
            findViewById<Button>(
                R.id.manageInternsButton
            )

        val createTaskButton =
            findViewById<Button>(
                R.id.createTaskButton
            )

        val allTasksButton =
            findViewById<Button>(
                R.id.allTasksButton
            )

        val progressButton =
            findViewById<Button>(
                R.id.progressButton
            )

        manageInternsButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ManageInternsActivity::class.java
                )
            )
        }

        createTaskButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddTaskActivity::class.java
                )
            )
        }

        allTasksButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SupervisorTasksActivity::class.java
                )
            )
        }

        progressButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProgressActivity::class.java
                )
            )
        }
    }

    override fun onResume() {

        super.onResume()

        loadDashboard()
    }

    private fun loadDashboard() {

        val totalInterns =
            database.getInterns().size

        val progress =
            database.getProgress()

        val total =
            progress[0]

        val completed =
            progress[1]

        val approved =
            progress[2]

        val rejected =
            progress[3]

        val pending =
            total -
                    completed -
                    approved -
                    rejected

        totalInternsText.text =
            totalInterns.toString()

        totalTasksText.text =
            total.toString()

        pendingTasksText.text =
            pending.toString()

        completedTasksText.text =
            completed.toString()

        approvedTasksText.text =
            approved.toString()

        rejectedTasksText.text =
            rejected.toString()
    }
}
