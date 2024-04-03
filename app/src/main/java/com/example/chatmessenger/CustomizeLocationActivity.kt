package com.example.chatmessenger

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class CustomizeLocationActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_location)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations")

        // Initialize table layout
        tableLayout = findViewById(R.id.tableLayout)

        // Fetch locations data
        fetchLocationsData()
    }

    private fun fetchLocationsData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (locationSnapshot in snapshot.children) {
                    val locationData = locationSnapshot.getValue(LocationData::class.java)
                    locationData?.let { data ->
                        // Create a new row for each location
                        val tableRow = TableRow(this@CustomizeLocationActivity)
                        val layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tableRow.layoutParams = layoutParams


                        val activityTextview = TextView(this@CustomizeLocationActivity)
                        activityTextview.text = data.activity
                        activityTextview.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        activityTextview.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        // Populate TextViews with location data
                        val myLocationTextView = TextView(this@CustomizeLocationActivity)
                        myLocationTextView.text = data.myLocation
                        myLocationTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        myLocationTextView.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        val advLocationTextView = TextView(this@CustomizeLocationActivity)
                        advLocationTextView.text = data.advLocation
                        advLocationTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        advLocationTextView.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        // Create ImageView for the image
                        val imageView = ImageView(this@CustomizeLocationActivity)
                        // Load image from URL using Picasso
                        Picasso.get().load(data.imageUrl).resize(200, 200).centerCrop().into(imageView)
                        val imageLayoutParams = TableRow.LayoutParams(200, 200)
                        imageView.layoutParams = imageLayoutParams
                        imageView.adjustViewBounds = true
                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

                        // Create LinearLayout for buttons
                        val buttonLayout = LinearLayout(this@CustomizeLocationActivity)
                        buttonLayout.orientation = LinearLayout.VERTICAL
                        buttonLayout.gravity = android.view.Gravity.CENTER

                        // Create Edit Button
                        val editButton = Button(this@CustomizeLocationActivity)
                        editButton.text = "Edit"
                        editButton.setTextColor(Color.WHITE)
                        editButton.setBackgroundColor(Color.parseColor("#00FF00")) // Shining 3D green color
                        val editLayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        editLayoutParams.setMargins(0, 10, 0, 10) // Add margin of 10dp
                        editButton.layoutParams = editLayoutParams
                        editButton.setOnClickListener {
                            // Create an Intent to launch EditLocationActivity
                            val intent = Intent(this@CustomizeLocationActivity, EditLocationsActivity::class.java)
                            // Pass the data of the corresponding row to the intent as extras
                            intent.putExtra("locationId", locationSnapshot.key)
                            intent.putExtra("activity", data.activity)
                            intent.putExtra("myLocation", data.myLocation)
                            intent.putExtra("advLocation", data.advLocation)
                            intent.putExtra("imageUrl", data.imageUrl)
                            // Start the EditLocationActivity with the intent
                            startActivity(intent)
                        }

                        // Create Delete Button
                        val deleteButton = Button(this@CustomizeLocationActivity)
                        deleteButton.text = "Delete"
                        deleteButton.setTextColor(Color.WHITE)
                        deleteButton.setBackgroundColor(Color.parseColor("#FF0000")) // Shining 3D red color
                        val deleteLayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        deleteLayoutParams.setMargins(0, 10, 0, 10) // Add margin of 10dp
                        deleteButton.layoutParams = deleteLayoutParams
                        deleteButton.setOnClickListener {
                            // Remove row from table
                            tableLayout.removeView(tableRow)
                            // Delete data from Firebase Database
                            locationSnapshot.ref.removeValue()
                            Toast.makeText(this@CustomizeLocationActivity, "Deleted ${data.myLocation}", Toast.LENGTH_SHORT).show()
                        }

                        // Add Buttons to the LinearLayout
                        buttonLayout.addView(editButton)
                        buttonLayout.addView(deleteButton)

                        // Add TextViews and ImageView to the TableRow
                        tableRow.addView(activityTextview)
                        tableRow.addView(myLocationTextView)
                        tableRow.addView(advLocationTextView)
                        tableRow.addView(imageView)

                        // Add LinearLayout with buttons to the TableRow
                        tableRow.addView(buttonLayout)

                        // Add TableRow to the TableLayout
                        tableLayout.addView(tableRow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database errors
                Toast.makeText(this@CustomizeLocationActivity, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
