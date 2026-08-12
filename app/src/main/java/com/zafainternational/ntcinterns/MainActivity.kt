package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button

    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        database = DatabaseHelper(this)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        roleSpinner = findViewById(R.id.roleSpinner)
        loginButton = findViewById(R.id.loginButton)
        createAccountButton = findViewById(R.id.createAccountButton)

        // =====================================
        // ROLES
        // =====================================

        val roles = arrayOf(
            getString(R.string.role_supervisor),
            getString(R.string.role_intern)
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        roleSpinner.adapter = adapter

        // =====================================
        // LOGIN
        // =====================================

        loginButton.setOnClickListener {

            val email =
                emailInput.text.toString().trim()

            val password =
                passwordInput.text.toString().trim()

            val role =
                roleSpinner.selectedItem.toString()

            // =====================================
            // EMAIL CHECK
            // =====================================

            if (email.isEmpty()) {

                emailInput.error =
                    getString(R.string.error_email)

                emailInput.requestFocus()

                return@setOnClickListener
            }

            // =====================================
            // PASSWORD CHECK
            // =====================================

            if (password.isEmpty()) {

                passwordInput.error =
                    getString(R.string.error_password)

                passwordInput.requestFocus()

                return@setOnClickListener
            }

            // =====================================
            // LOGIN FROM DATABASE
            // =====================================

            val userId =
                database.loginUser(
                    email,
                    password,
                    role
                )

            // =====================================
            // LOGIN SUCCESS
            // =====================================

            if (userId != -1) {

                if (
                    role ==
                    getString(R.string.role_supervisor)
                ) {

                    // =================================
                    // SUPERVISOR
                    // =================================

                    val intent =
                        Intent(
                            this,
                            SupervisorActivity::class.java
                        )

                    intent.putExtra(
                        "userId",
                        userId
                    )

                    intent.putExtra(
                        "email",
                        email
                    )

                    startActivity(intent)

                    finish()

                } else {

                    // =================================
                    // INTERN
                    // =================================

                    val intent =
                        Intent(
                            this,
                            InternActivity::class.java
                        )

                    intent.putExtra(
                        "internId",
                        userId
                    )

                    intent.putExtra(
                        "userId",
                        userId
                    )

                    startActivity(intent)

                    finish()
                }

            } else {

                // =====================================
                // INVALID LOGIN
                // =====================================

                Toast.makeText(
                    this,
                    "Invalid email, password or role",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // =====================================
        // CREATE ACCOUNT
        // =====================================

        createAccountButton.setOnClickListener {

            val intent = Intent(
                this,
                CreateAccountActivity::class.java
            )

            startActivity(intent)
        }
    }
}
