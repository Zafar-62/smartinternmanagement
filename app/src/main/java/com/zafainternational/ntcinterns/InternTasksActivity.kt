package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class InternTasksActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper
    private lateinit var taskList: LinearLayout

    private var internId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_intern_tasks
        )

        database =
            DatabaseHelper(this)

        internId =
            intent.getIntExtra(
                "internId",
                -1
            )

        taskList =
            findViewById(R.id.taskList)
    }

    override fun onResume() {
        super.onResume()

        loadTasks()
    }

    private fun loadTasks() {

        taskList.removeAllViews()

        val tasks =
            database.getInternTasks(
                internId
            )

        if (tasks.isEmpty()) {

            val empty =
                TextView(this)

            empty.text =
                "No assigned tasks."

            empty.textSize = 18f

            taskList.addView(empty)

            return
        }

        for (item in tasks) {

            val parts =
                item.split("|")

            val id =
                parts[0].toInt()

            val title =
                parts[1]

            val deadline =
                parts[2]

            val status =
                parts[3]

            val text =
                TextView(this)

            text.text =
                "Task: $title\n" +
                        "Deadline: $deadline\n" +
                        "Status: $status"

            text.textSize = 17f

            text.setPadding(
                15,
                20,
                15,
                20
            )

            taskList.addView(text)

            val button =
                Button(this)

            button.text =
                "OPEN TASK"

            button.setOnClickListener {

                val intent =
                    Intent(
                        this,
                        TaskDetailActivity::class.java
                    )

                intent.putExtra(
                    "taskId",
                    id
                )

                startActivity(intent)
            }

            taskList.addView(button)
        }
    }
}