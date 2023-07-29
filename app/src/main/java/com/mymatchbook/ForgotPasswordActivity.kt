package com.mymatchbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnSubmit : Button = findViewById(R.id.btn_submit)
        val etForgotEmail : EditText = findViewById(R.id.et_forgotEmail)
        val tvLogin : TextView = findViewById(R.id.tv_login)

        // On Click Listener that allows user to send reset email once conditions are met
        btnSubmit.setOnClickListener{

            val email : String = etForgotEmail.text.toString().trim{ it <= ' '}

            if (email.isEmpty()){
                myToast("Please enter email address")
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                            task ->
                        if (task.isSuccessful){
                            myToast("Email Sent")
                            finish()
                        }else{
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        // On Click Listener that takes the user back to the login page
        tvLogin.setOnClickListener(){
            goToLoginActivity()
        }
    }

    // Method that takes the user back to the main activity if they have an account
    private fun goToLoginActivity(){
        val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method for Toasts
    private fun myToast(message : String){
        Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
    }
}


