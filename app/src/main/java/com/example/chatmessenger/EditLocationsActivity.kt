package com.example.chatmessenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditLocationsActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_locations)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations")

        // Retrieve data passed from CustomizeLocationsActivity
        val locationId = intent.getStringExtra("locationId")
        val activity = intent.getStringExtra("activity")
        val myLocation = intent.getStringExtra("myLocation")
        val advLocation = intent.getStringExtra("advLocation")

        // Initialize UI elements
        val activityEditText = findViewById<EditText>(R.id.activityEditText)
        val myLocationEditText = findViewById<EditText>(R.id.myLocationEditText)
        val advLocationEditText = findViewById<EditText>(R.id.advLocationEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set data to UI elements
        activityEditText.setText(activity)
        myLocationEditText.setText(myLocation)
        advLocationEditText.setText(advLocation)

        // Set onClickListener for submitButton
        submitButton.setOnClickListener {
            // Get updated values from EditText fields
            val updatedActivity = activityEditText.text.toString()
            val updatedMyLocation = myLocationEditText.text.toString()
            val updatedAdvLocation = advLocationEditText.text.toString()

            // Update the data in Firebase Database
            updateData(locationId, updatedActivity, updatedMyLocation, updatedAdvLocation)

            // Display a toast message to indicate successful submission
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }
    }

    private fun updateData(locationId: String?,updatedActivity:String?, updatedMyLocation: String, updatedAdvLocation: String) {
        // Get the reference of the specific location to update
        val query: Query = databaseReference.orderByChild("id").equalTo(locationId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (locationSnapshot in dataSnapshot.children) {
                    // Update the data in Firebase Database
                    locationSnapshot.ref.child("activity").setValue(updatedActivity)
                    locationSnapshot.ref.child("myLocation").setValue(updatedMyLocation)
                    locationSnapshot.ref.child("advLocation").setValue(updatedAdvLocation)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors
                Toast.makeText(this@EditLocationsActivity, "Failed to update data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
