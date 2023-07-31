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
    private var email : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Variables for Activity components
        val tvEmail : TextView = findViewById(R.id.tv_welcomeUserEmail)
        val ivAddMatch : ImageView = findViewById(R.id.iv_addMatch)
        val ivDeleteMatch : ImageView = findViewById(R.id.iv_deleteMatch)
        val ivViewMatch : ImageView = findViewById(R.id.iv_viewMatch)
        val ivAnalyzeMatch : ImageView = findViewById(R.id.iv_analyzeMatch)
        val btnLogout : Button = findViewById(R.id.btn_logout)

        // retrieves User's email from login screen and displays it in the welcome msg
        email = intent.getStringExtra(Constants.USER_EMAIL)
        tvEmail.text = "$email"

        // On click function that takes the user to an activity
        ivAddMatch.setOnClickListener{
            createMatch()}

        ivDeleteMatch.setOnClickListener{
            startActivity(Intent(this@MainActivity, DeleteMatchActivity::class.java))
        }

        ivViewMatch.setOnClickListener{
            startActivity(Intent(this@MainActivity, ViewMatchActivity::class.java))
        }

        ivAnalyzeMatch.setOnClickListener{
            startActivity(Intent(this@MainActivity, AnalyzeMatchActivity::class.java))
        }

        btnLogout.setOnClickListener{
            myToast("You have been logged out!")
            goToLoginActivity()
        }
    }

    // This function will create a match in the firebase database and set the data
    private fun createMatch(){
        val intent = Intent(this@MainActivity, AddMatchActivity::class.java)
        intent.putExtra(Constants.USER_EMAIL, email)
        startActivity(intent)
    }

    // Method that allows the user to logout and go back to the login screen
    fun goToLoginActivity(){
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method for Toasts
    private fun myToast(message : String){
        Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
    }
}