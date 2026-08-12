
package com.zafainternational.ntcinterns

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var createAccountButton: Button
    private lateinit var backToLoginButton: Button

    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        database = DatabaseHelper(this)

        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        roleSpinner = findViewById(R.id.roleSpinner)
        createAccountButton = findViewById(R.id.createAccountButton)
        backToLoginButton = findViewById(R.id.backToLoginButton)

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

        createAccountButton.setOnClickListener {

            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword =
                confirmPasswordInput.text.toString()

            val role = roleSpinner.selectedItem.toString()

            if (name.isEmpty()) {
                nameInput.error = "Enter your name"
                nameInput.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailInput.error = "Enter your email"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {
                emailInput.error = "Enter a valid email"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordInput.error = "Enter password"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 4) {
                passwordInput.error =
                    "Password must be at least 4 characters"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordInput.error =
                    "Re-enter password"
                confirmPasswordInput.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordInput.error =
                    "Passwords do not match"

                Toast.makeText(
                    this,
                    "Password and Confirm Password must be the same",
                    Toast.LENGTH_SHORT
                ).show()

                confirmPasswordInput.requestFocus()
                return@setOnClickListener
            }

            val success = database.createAccount(
                name = name,
                email = email,
                password = password,
                role = role
            )

            if (success) {

                Toast.makeText(
                    this,
                    "$role account created successfully",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(
                    this,
                    MainActivity::class.java
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)
                finish()

            } else {

                Toast.makeText(
                    this,
                    "This email is already registered",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        backToLoginButton.setOnClickListener {

            val intent = Intent(
                this,
                MainActivity::class.java
            )

            startActivity(intent)
            finish()
        }
    }
}
