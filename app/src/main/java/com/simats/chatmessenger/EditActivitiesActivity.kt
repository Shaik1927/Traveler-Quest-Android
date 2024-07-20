package com.simats.chatmessenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditActivitiesActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_activities)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Activities")

        // Retrieve data passed from CustomizeActivitiesActivity
        val categoryId = intent.getStringExtra("categoryId")
        val activityName = intent.getStringExtra("activityName")

        // Initialize UI elements
        val homeActivityEditText = findViewById<EditText>(R.id.homeactivity)
        val homeCategoryEditText = findViewById<EditText>(R.id.homecategory)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set data to UI elements
        homeActivityEditText.setText(activityName)
        homeCategoryEditText.setText(categoryId)

        // Set onClickListener for submitButton
        submitButton.setOnClickListener {
            // Get updated values from EditText fields
            val updatedActivityName = homeActivityEditText.text.toString()
            val updatedCategory = homeCategoryEditText.text.toString()

            // Update the data in Firebase Database
            updateData(updatedActivityName, updatedCategory)

            // Display a toast message to indicate successful submission
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }
    }

    private fun updateData(updatedActivityName: String, updatedCategory: String) {
        // Get the reference of the specific activity to update
        val query: Query = databaseReference.orderByChild("activity").equalTo(intent.getStringExtra("activityName"))

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activitySnapshot in dataSnapshot.children) {
                    // Update the data in Firebase Database
                    val key = activitySnapshot.key
                    if (key != null) {
                        databaseReference.child(key).child("activity").setValue(updatedActivityName)
                        databaseReference.child(key).child("category").setValue(updatedCategory)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors
                Toast.makeText(this@EditActivitiesActivity, "Failed to update data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
