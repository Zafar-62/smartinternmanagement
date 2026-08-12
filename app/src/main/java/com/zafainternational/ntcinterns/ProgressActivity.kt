package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProgressActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_progress
        )

        database =
            DatabaseHelper(this)

        loadProgress()
    }

    override fun onResume() {

        super.onResume()

        loadProgress()
    }

    private fun loadProgress() {

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

        val percentage =
            if (total > 0) {

                (approved * 100) / total

            } else {

                0
            }

        findViewById<TextView>(
            R.id.progressText
        ).text =
            "Overall Progress: $percentage%"

        findViewById<TextView>(
            R.id.totalText
        ).text =
            "Total Tasks: $total"

        findViewById<TextView>(
            R.id.pendingText
        ).text =
            "Pending Tasks: $pending"

        findViewById<TextView>(
            R.id.completedText
        ).text =
            "Completed Tasks: $completed"

        findViewById<TextView>(
            R.id.approvedText
        ).text =
            "Approved Tasks: $approved"

        findViewById<TextView>(
            R.id.rejectedText
        ).text =
            "Rejected Tasks: $rejected"
    }
}
