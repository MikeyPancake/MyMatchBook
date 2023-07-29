package com.mymatchbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //Email declared a global variable so that other methods can access it
    private lateinit var etEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_email)
        val etPassword : TextView = findViewById(R.id.et_password)
        val loginButton : Button = findViewById(R.id.btn_login)

        auth = FirebaseAuth.getInstance()

        // On click listener that checks if email and/or password are entered
        loginButton.setOnClickListener{
            // login parameters
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // If statement that catches if user does not enter a email or password
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            }else{
                // Toast if missing email and/or password
                myToast("Please enter Email and Password")
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
                    myToast("Login Success")
                    // Takes user to main activity once login is successful
                    goToMainActivity()
                } else {
                    // Login failed
                    myToast("login Failed")
                }
            }
    }

    // Method that takes the user to the create account activity if they do not have an account
    fun goToCreateAccountActivity(view : View){
        val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method that takes the user to the forgot password activity
    fun goToPasswordResetActivity(view : View){
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java )
        startActivity(intent)
        finish()
    }

    // Method that takes user to Main Activity
    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        // intent that sends the User's Email address to the main activity
        intent.putExtra("EMAIL", etEmail.text.toString())
        startActivity(intent)
        finish()
    }

    // Method for Toasts
    private fun myToast(message : String){
        Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
    }
}