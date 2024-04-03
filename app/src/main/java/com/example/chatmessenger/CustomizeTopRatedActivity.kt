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

class CustomizeTopRatedActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_top_rated)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Home")

        // Initialize table layout
        tableLayout = findViewById(R.id.tableLayout)

        // Fetch locations data
        fetchLocationsData()
    }

    private fun fetchLocationsData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (homeSnapshot in snapshot.children) {
                    val homeData = homeSnapshot.getValue(HomeData::class.java)
                    homeData?.let { data ->
                        // Create a new row for each location
                        val tableRow = TableRow(this@CustomizeTopRatedActivity)
                        val layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tableRow.layoutParams = layoutParams

                        val activityTextview = TextView(this@CustomizeTopRatedActivity)
                        activityTextview.text = data.activty
                        activityTextview.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        activityTextview.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        // Populate TextViews with location data
                        val ratingTextView = TextView(this@CustomizeTopRatedActivity)
                        ratingTextView.text = data.rating
                        ratingTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        ratingTextView.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )


                        // Create ImageView for the image
                        val imageView = ImageView(this@CustomizeTopRatedActivity)
                        // Load image from URL using Picasso
                        Picasso.get().load(data.imageUrl).resize(200, 200).centerCrop().into(imageView)
                        val imageLayoutParams = TableRow.LayoutParams(200, 200)
                        imageView.layoutParams = imageLayoutParams
                        imageView.adjustViewBounds = true
                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

                        // Create LinearLayout for buttons
                        val buttonLayout = LinearLayout(this@CustomizeTopRatedActivity)
                        buttonLayout.orientation = LinearLayout.VERTICAL
                        buttonLayout.gravity = android.view.Gravity.CENTER

                        // Create Edit Button
                        val editButton = Button(this@CustomizeTopRatedActivity)
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
                            val intent = Intent(this@CustomizeTopRatedActivity, EditTopRateActivity::class.java)
                            // Pass the data of the corresponding row to the intent as extras
                            intent.putExtra("homeId", homeSnapshot.key)
                            intent.putExtra("activty", data.activty)
                            intent.putExtra("rating", data.rating)
                            intent.putExtra("imageUrl", data.imageUrl)
                            // Start the EditLocationActivity with the intent
                            startActivity(intent)
                        }

                        // Create Delete Button
                        val deleteButton = Button(this@CustomizeTopRatedActivity)
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
                            homeSnapshot.ref.removeValue()
                            Toast.makeText(this@CustomizeTopRatedActivity, "Deleted ${data.activty}", Toast.LENGTH_SHORT).show()
                        }

                        // Add Buttons to the LinearLayout
                        buttonLayout.addView(editButton)
                        buttonLayout.addView(deleteButton)

                        // Add TextViews and ImageView to the TableRow
                        tableRow.addView(activityTextview)
                        tableRow.addView(ratingTextView)
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
                Toast.makeText(this@CustomizeTopRatedActivity, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
