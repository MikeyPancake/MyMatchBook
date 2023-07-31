package com.mymatchbook

import android.app.DatePickerDialog
import android.content.Intent
import java.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddMatchActivity : AppCompatActivity() {


    // Variables for Activity components
    private lateinit var matchName : EditText
    private lateinit var matchDate : EditText
    private lateinit var matchLocation : EditText
    private lateinit var matchType : EditText
    private lateinit var matchSeason : EditText

    // Database variables
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // Variable for user's email intent
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)

        // Activity variables
        matchName = findViewById(R.id.et_matchName)
        matchDate = findViewById(R.id.et_matchDate)
        matchLocation = findViewById(R.id.et_matchLocation)
        matchType = findViewById(R.id.et_matchType)
        matchSeason = findViewById(R.id.et_matchSeason)
        val btnCreateMatch : Button = findViewById(R.id.btn_createMatch)
        val btnBack : Button = findViewById(R.id.btn_backToDashboard)

        // Firebase authentication
        auth = FirebaseAuth.getInstance()
        // Gets instance of database and sets parent node to "My Match Book"
        dbRef = FirebaseDatabase.getInstance().getReference("My Match Book")


        // Set the OnFocusChangeListener for each TextInputEditText
        // This will add a custom hint once user clicks on text box
        setHintForTextInputEditText(findViewById(R.id.et_matchName), "e.g. Gap Grind")
        setHintForTextInputEditText(findViewById(R.id.et_matchLocation), "e.g. New York, New York")
        setHintForTextInputEditText(findViewById(R.id.et_matchType), "e.g. PRS, Outlaw, Other")
        setHintForTextInputEditText(findViewById(R.id.et_matchSeason), "e.g. 2023")
        setHintForTextInputEditText(findViewById(R.id.et_matchStages), "e.g. 20")

        // On click listeners for user input fields and buttons
        matchDate.setOnClickListener{
            showDatePickerDialog()
        }
        btnBack.setOnClickListener{
            goToMainActivity()
        }
        btnCreateMatch.setOnClickListener{
            createMatch()
        }

    }

    // Method to insert data into Database
    private fun createMatch() {
        // gets values
        val name = matchName.text.toString()
        val date = matchDate.text.toString()
        val location = matchLocation.text.toString()
        val type = matchType.text.toString()
        val season = matchSeason.text.toString()

        // Check if the user is authenticated. If not, redirect to login activity.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User is not logged in. Redirect to login activity.
            goToLoginActivity()
            return
        }

        // User is logged in. Proceed with adding match details.
        val userID = currentUser.uid

        // If Statement to check if fields are empty
        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || type.isEmpty() || season.isEmpty()) {
            myToast("All Fields Required")
            return
        }

        // sets match unique ID and has a null check
        val matchID = dbRef.push().key!!
        // Sets values of a match using data class
        val match = Match(matchID, name, date, location, type, season)

        // Check if the user already has a competition type node for the match type
        dbRef.child("users").child(userID).child("competitions").child(type)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // If A competition type node already exists for the match type,
                        // add the match to that competition type
                        val competitionType = snapshot.getValue(CompetitionType::class.java)
                        competitionType?.let {
                            it.matches = it.matches + match // Append new match to the list
                        }

                        dbRef
                            .child("users")
                            .child(userID)
                            .child("competitions")
                            .child(type)
                            .setValue(competitionType)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Match added to the existing competition type node
                                    myToast("Match Inserted Successfully")
                                    goToMainActivity()
                                } else {
                                    myToast("Unable to add match")
                                }
                            }
                    }
                    else {
                        // No competition type node for the match type exists, create a new one
                        val newCompetitionType = CompetitionType(type, listOf(match))
                        val competitionsRef = dbRef
                            .child("users")
                            .child(userID)
                            .child("competitions")
                            .child(type)
                        competitionsRef.setValue(newCompetitionType)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // New competition type node created with the match
                                    myToast("Match Inserted Successfully")
                                    goToMainActivity()
                                } else {
                                    myToast("Unable to add match")
                                }
                            }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if retrieval fails
                    myToast("Error: ${error.message}")
                }
            }
            )
    }

    // Method that sets calendar for user to pick match date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date
                handleSelectedDate(selectedYear, selectedMonth, selectedDay)
            },
            year,
            month,
            day
        )
        // Optionally sets maximum date for the picker to 7 days
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)
        datePickerDialog.show()
    }

    // Method that handles calendar input and saves it in a text format
    private fun handleSelectedDate(year: Int, month: Int, day: Int) {
        // TODO - I want to set this information into the data base
        val selectedDate = "$year-${month + 1}-$day"
        matchDate.setText(selectedDate)
    }

    // Method that takes user to Main Activity when back to dash board is clicked
    private fun goToMainActivity(){
        // Retrieve user's email from the intent extras and pushes the intent back to MainActivity
        val email = intent.getStringExtra(Constants.USER_EMAIL)
        val intent = Intent(this@AddMatchActivity, MainActivity::class.java)
        intent.putExtra(Constants.USER_EMAIL, email) // Pass the user's email back
        startActivity(intent)
        finish()
    }

    // Method that allows the user to go back to the login screen
    private fun goToLoginActivity(){
        val intent = Intent(this@AddMatchActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method that will set the hint once a user clicks on a text box
    private fun setHintForTextInputEditText(editText: EditText, hint: String) {
        val textInputLayout = editText.parent.parent as TextInputLayout
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                textInputLayout.hint = hint
            } else {
                textInputLayout.hint = ""
            }
        }
    }

    // Method for Toasts
    private fun myToast(message : String){
        Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
    }
}

