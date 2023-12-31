package com.mymatchbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val btnCreateAccount : Button = findViewById(R.id.btn_createAccount)
        val etEmail : EditText = findViewById(R.id.et_email)
        val etPassword : EditText = findViewById(R.id.et_password)
        val etConfirmPassword : EditText = findViewById(R.id.et_confirmPassword)
        val tvAlreadyHaveAccount : TextView = findViewById(R.id.tv_login)

        auth = FirebaseAuth.getInstance()

        // On click Listener that checks to see if email and password are entered
        btnCreateAccount.setOnClickListener {
            // Variables for email/password
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Checks if either email, password, confirmation password are empty
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // Checks if password and confirmation password are the same
                if (password == confirmPassword){
                    createAccount(email, password)
                }else{
                    myToast("Password Does Not Match")
                }
            } else {
                myToast("Please Input Required Fields")
            }
        }

        // On Click Listener that takes the user back to the login page if they already have an account
        tvAlreadyHaveAccount.setOnClickListener(){
            goToLoginActivity()
        }
    }

    // Method that creates a user account using Firebase
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                    task ->
                    if (task.isSuccessful) {
                        // Account creation success
                        val user = auth.currentUser
                        //TODO - Delete once test are successfull
                        myToast("Create Account Success")
                        // After successful account creation, go back to the login activity
                        goToLoginActivity()
                    } else {
                        // Account creation failed
                        // You can handle specific failure cases here, such as invalid email format, weak password, etc.
                        myToast("Create Account Failed")
                    }
                }
    }

    // Method that takes the user back to the main activity if they have an account
    private fun goToLoginActivity(){
        val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method that takes user to Main Activity
    private fun goToMainActivity(){
        val intent = Intent(this@CreateAccountActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Toast Method
    private fun myToast(message : String){
        Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
    }


}