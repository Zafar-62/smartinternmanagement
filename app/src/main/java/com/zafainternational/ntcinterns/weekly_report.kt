
package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class WeeklyReportActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_weekly_report
        )

        database =
            DatabaseHelper(this)

        val internId =
            intent.getIntExtra(
                "internId",
                -1
            )

        val week =
            findViewById<EditText>(
                R.id.weekInput
            )

        val completed =
            findViewById<EditText>(
                R.id.completedInput
            )

        val skills =
            findViewById<EditText>(
                R.id.skillsInput
            )

        val problems =
            findViewById<EditText>(
                R.id.problemsInput
            )

        val nextPlan =
            findViewById<EditText>(
                R.id.nextPlanInput
            )

        findViewById<Button>(
            R.id.submitButton
        ).setOnClickListener {

            if (internId == -1) {
                return@setOnClickListener
            }

            if (
                week.text.toString().trim().isEmpty() ||
                completed.text.toString().trim().isEmpty() ||
                skills.text.toString().trim().isEmpty() ||
                nextPlan.text.toString().trim().isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please complete required fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val db =
                database.writableDatabase

            val values =
                android.content.ContentValues().apply {

                    put(
                        "intern_id",
                        internId
                    )

                    put(
                        "week",
                        week.text.toString().trim()
                    )

                    put(
                        "completed_tasks",
                        completed.text.toString().trim()
                    )

                    put(
                        "skills",
                        skills.text.toString().trim()
                    )

                    put(
                        "problems",
                        problems.text.toString().trim()
                    )

                    put(
                        "next_plan",
                        nextPlan.text.toString().trim()
                    )

                    put(
                        "status",
                        "Submitted"
                    )
                }

            val result =
                db.insert(
                    "weekly_reports",
                    null,
                    values
                )

            if (result != -1L) {

                Toast.makeText(
                    this,
                    "Weekly report submitted",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Submission failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
