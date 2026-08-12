package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val feedbackText = findViewById<TextView>(
            R.id.feedbackText
        )

        feedbackText.text ="Supervisor Feedback\n\n" + "No feedback available yet."
    }
}