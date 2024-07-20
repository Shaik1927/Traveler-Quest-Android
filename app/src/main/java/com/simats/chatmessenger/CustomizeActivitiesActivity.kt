package com.simats.chatmessenger

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import android.widget.*
import android.graphics.Color
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast


class CustomizeActivitiesActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_activities)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Activities")

        // Initialize table layout
        tableLayout = findViewById(R.id.tableLayout)

        // Fetch activities data
        fetchActivitiesData()
    }

    private fun fetchActivitiesData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (activitySnapshot in snapshot.children) {
                    val catactData = activitySnapshot.getValue(CatActData::class.java)
                    catactData?.let { data ->
                        // Create a new row for each activity
                        val tableRow = TableRow(this@CustomizeActivitiesActivity)
                        val layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tableRow.layoutParams = layoutParams

                        // Populate TextViews with activity data
                        val categoryTextView = TextView(this@CustomizeActivitiesActivity)
                        categoryTextView.text = data.category
                        categoryTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        categoryTextView.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        val activityNameTextView = TextView(this@CustomizeActivitiesActivity)
                        activityNameTextView.text = data.activity
                        activityNameTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        activityNameTextView.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )

                        // Create ImageView for the image
                        val imageView = ImageView(this@CustomizeActivitiesActivity)
                        // Load image from URL using Picasso
                        Picasso.get().load(data.imageUrl).resize(200, 200).centerCrop().into(imageView)
                        val imageLayoutParams = TableRow.LayoutParams(200, 200)
                        imageView.layoutParams = imageLayoutParams
                        imageView.adjustViewBounds = true
                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

                        // Create LinearLayout for buttons
                        val buttonLayout = LinearLayout(this@CustomizeActivitiesActivity)
                        buttonLayout.orientation = LinearLayout.VERTICAL
                        buttonLayout.gravity = android.view.Gravity.CENTER

                        // Create Edit Button
                        val editButton = Button(this@CustomizeActivitiesActivity)
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
                            // Create an Intent to launch EditActivitiesActivity
                            val intent = Intent(this@CustomizeActivitiesActivity, EditActivitiesActivity::class.java)
                            // Pass the data of the corresponding row to the intent as extras
                            intent.putExtra("categoryId", data.category)
                            intent.putExtra("activityName", data.activity)
                            intent.putExtra("imageUrl", data.imageUrl)
                            // Start the EditActivitiesActivity with the intent
                            startActivity(intent)
                        }

                        // Create Delete Button
                        val deleteButton = Button(this@CustomizeActivitiesActivity)
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
                            activitySnapshot.ref.removeValue()
                            Toast.makeText(this@CustomizeActivitiesActivity, "Deleted ${data.activity}", Toast.LENGTH_SHORT).show()
                        }

                        // Add Buttons to the LinearLayout
                        buttonLayout.addView(editButton)
                        buttonLayout.addView(deleteButton)

                        // Add TextViews and ImageView to the TableRow
                        tableRow.addView(categoryTextView)
                        tableRow.addView(activityNameTextView)
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
            }
        })
    }
}
