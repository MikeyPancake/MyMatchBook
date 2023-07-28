package com.mymatchbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnSubmit : Button = findViewById(R.id.btn_submit)
        val etForgotEmail : EditText = findViewById(R.id.et_forgotEmail)

        btnSubmit.setOnClickListener{

            val email : String = etForgotEmail.text.toString().trim{ it <= ' '}

            if (email.isEmpty()){
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please enter email address",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                            task ->
                        if (task.isSuccessful){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Email Sent",
                                Toast.LENGTH_LONG
                            ).show()
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
    }
}