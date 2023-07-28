package com.mymatchbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnCreateAccount : Button = findViewById(R.id.btn_createAccount)

        btnLogin.setOnClickListener{
            goToLoginActivity()
        }

        btnCreateAccount.setOnClickListener{
            goToCreateAccountActivity()
        }
    }

    // Method that takes user to login Activity
    private fun goToLoginActivity(){
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    // Method that takes user to Create Account Activity
    private fun goToCreateAccountActivity(){
        val intent = Intent(this@SplashActivity, CreateAccountActivity::class.java)
        startActivity(intent)
        finish()
    }
}