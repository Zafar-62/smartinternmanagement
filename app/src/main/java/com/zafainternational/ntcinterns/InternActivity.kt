package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InternActivity : AppCompatActivity() {

    private var internId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_intern,
        )

        internId =
            intent.getIntExtra(
                "internId",
                -1
            )

        val tasksButton =
            findViewById<Button>(
                R.id.tasksButton
            )

        val logoutButton =
            findViewById<Button>(
                R.id.logoutButton
            )

        tasksButton.setOnClickListener {

            val intent =
                Intent(
                    this,
                    InternTasksActivity::class.java
                )

            intent.putExtra(
                "internId",
                internId
            )

            startActivity(intent)
        }

        logoutButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }
}