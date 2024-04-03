package com.example.chatmessenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditTopRateActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_top_rate)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Home")

        // Retrieve data passed from CustomizeTopRatedActivity
        val homeId = intent.getStringExtra("homeId")
        val activity = intent.getStringExtra("activty")
        val rating = intent.getStringExtra("rating")

        // Initialize UI elements
        val activityEditText = findViewById<EditText>(R.id.activityEditText)
        val ratingEditText = findViewById<EditText>(R.id.ratingEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set data to UI elements
        activityEditText.setText(activity)
        ratingEditText.setText(rating)

        // Set onClickListener for submitButton
        submitButton.setOnClickListener {
            // Get updated values from EditText fields
            val updatedActivity = activityEditText.text.toString()
            val updatedRating = ratingEditText.text.toString()

            // Update the data in Firebase Database
            updateData(homeId, updatedActivity, updatedRating)

            // Display a toast message to indicate successful submission
            Toast.makeText(this@EditTopRateActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }
    }

    private fun updateData(homeId: String?, updatedActivity: String, updatedRating: String) {
        // Get the reference of the specific top-rated item to update
        val query: Query = databaseReference.orderByChild("id").equalTo(homeId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (homeSnapshot in dataSnapshot.children) {
                    // Update the data in Firebase Database
                    homeSnapshot.ref.child("activty").setValue(updatedActivity)
                    homeSnapshot.ref.child("rating").setValue(updatedRating)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors
                Toast.makeText(this@EditTopRateActivity, "Failed to update data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
