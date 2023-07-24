package com.mymatchbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail : TextView = findViewById(R.id.et_email)
        val etPassword : TextView = findViewById(R.id.et_password)
        val loginButton : MaterialButton = findViewById(R.id.btn_login)
        auth = FirebaseAuth.getInstance()

        // On click listener that checks if email and/or password are entered
        loginButton.setOnClickListener{
            // login parameters
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // If statement that catches if user does not enter a email or password
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(
                    email,
                    password
                )
            }else{
                // Toast if missing email and/or password
                Toast.makeText(this,
                    "Please enter Email and Password",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }
    // Method that checks if email and password are correct based on firebase database
    private fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnCompleteListener(this) {
                    task ->
                    if (task.isSuccessful) {
                        // Login success
                        val user = auth.currentUser
                        // You can do additional actions after successful login, such as navigating to another activity
                        Toast.makeText(this,
                            "Login Successful",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // Login failed
                        // You can handle specific failure cases here, such as invalid email/password, user not found, etc.
                        Toast.makeText(this,
                            "Login Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
    }

    // Method that takes the user to the create account activity if they do not have an account
    fun goToCreateAccountActivity(view : View){
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
        finish()
    }
}