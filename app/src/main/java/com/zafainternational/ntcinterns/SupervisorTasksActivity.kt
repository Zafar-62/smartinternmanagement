package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SupervisorTasksActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper
    private lateinit var taskList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_supervisor_tasks
        )

        database =
            DatabaseHelper(this)

        taskList =
            findViewById(R.id.taskList)

        taskList.setOnItemClickListener {
                _, _, position, _ ->

            val tasks =
                database.getAllTasks()

            if (position < tasks.size) {

                val parts =
                    tasks[position].split("|")

                if (parts.isNotEmpty()) {

                    val taskId =
                        parts[0].toInt()

                    val intent =
                        Intent(
                            this,
                            ReviewTaskActivity::class.java
                        )

                    intent.putExtra(
                        "taskId",
                        taskId
                    )

                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {

        super.onResume()

        loadTasks()
    }

    private fun loadTasks() {

        val tasks =
            database.getAllTasks()

        val displayList =
            ArrayList<String>()

        for (task in tasks) {

            val parts =
                task.split("|")

            if (parts.size >= 5) {

                val id = parts[0]
                val title = parts[1]
                val intern = parts[2]
                val deadline = parts[3]
                val status = parts[4]

                displayList.add(
                    """
                    Task #$id
                    $title
                    Intern: $intern
                    Deadline: $deadline
                    Status: $status
                    """.trimIndent()
                )
            }
        }

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                displayList
            )

        taskList.adapter =
            adapter
    }
}
