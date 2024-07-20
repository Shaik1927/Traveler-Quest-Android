package com.simats.chatmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class TerrainsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var coversContainer: LinearLayout
    private val activitiesReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Activities")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terrains)

        coversContainer = findViewById(R.id.coversContainer)

        // Show covers with category == "mountain"
        showMountainCovers()
    }

    override fun onClick(view: View) {
        // Handle clicks if needed
    }

    private fun showMountainCovers() {
        // Modify the query to fetch data where category == "mountain"
        val mountainQuery = activitiesReference.orderByChild("category").equalTo("Terrains")

        mountainQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("CatActData", "No data found for mountains")
                    return
                }

                for (mountainSnapshot in snapshot.children) {
                    val catActData = mountainSnapshot.getValue(CatActData::class.java)

                    if (catActData != null) {
                        val imageUrl = catActData.imageUrl

                        if (!imageUrl.isNullOrEmpty()) {

                            val activity = catActData.activity

                            val coverItemView: View = layoutInflater.inflate(R.layout.activity_catactfetch, coversContainer, false)
                            val coverImageView: ImageView = coverItemView.findViewById(R.id.coverImageView)
                            val activityTextView: TextView = coverItemView.findViewById(R.id.activityTextView)

                            // Set data for the ImageView using Picasso
                            Picasso.get().load(imageUrl).into(coverImageView)

                            activityTextView.text = activity

                            // Add click listener to each cover container
                            coverItemView.setOnClickListener {
                                handleCoverContainerClick(activity)
                            }

                            // Add the inflated layout to the linear layout
                            coversContainer.addView(coverItemView)
                        } else {
                            Log.e("CatActData", "Image URL is null or empty for an entry")
                        }
                    } else {
                        Log.e("CatActData", "Failed to parse CatActData for an entry")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CatActData", "Database error: ${error.message}")
            }
        })
    }

    private fun handleCoverContainerClick(activityType: String?) {
        when (activityType) {
            "Trekking" -> redirectToActivity(TrekkingActivity::class.java)
            "Horse Riding " -> redirectToActivity(HorseridingActivity::class.java)
            "Horse Riding" -> redirectToActivity(HorseridingActivity::class.java)
            "Paragliding" -> redirectToActivity(ParaglidingActivity::class.java)
            "Hot Air Balloon" -> redirectToActivity(HotairballoonActivity::class.java)
            "Wakeboarding" -> redirectToActivity(WakeboardingActivity::class.java)
            "Bike Trips" -> redirectToActivity(BiketripActivity::class.java)
            "Sky Diving" -> redirectToActivity(SkydivingActivity::class.java)
            "Paramotoring" -> redirectToActivity(ParamotoringActivity::class.java)
            "Scuba Diving" -> redirectToActivity(ScubadivingActivity::class.java)
            "Surfing" -> redirectToActivity(SurfingActivity::class.java)
            "Kayaking" -> redirectToActivity(KayakingActivity::class.java)
            "River Rafting" -> redirectToActivity(RiverraftingActivity::class.java)
            "Helicopter Ride" -> redirectToActivity(HelicopterActivity::class.java)
            "Parasailing" -> redirectToActivity(ParasailingActivity::class.java)
            "Safari" -> redirectToActivity(SafariActivity::class.java)
            "Rock Climbing" -> redirectToActivity(RockclimbingActivity::class.java)
            "Hiking" -> redirectToActivity(HikingActivity::class.java)
            "Camel Riding" -> redirectToActivity(CamelridingActivity::class.java)
            "Boating" -> redirectToActivity(BoatingActivity::class.java)
            "Go Karting" -> redirectToActivity(GokartingActivity::class.java)
            "Water Parks" -> redirectToActivity(WaterparksActivity::class.java)
            "Paint Ball" -> redirectToActivity(PaintballActivity::class.java)
            "Stargazing " -> redirectToActivity(StargazingActivity::class.java)
            "Camping" -> redirectToActivity(CampingActivity::class.java)
            "Mountain Biking" -> redirectToActivity(MountainbikeActivity::class.java)
            "Mountain Carting" -> redirectToActivity(MountaincartActivity::class.java)
            "Mud Carting" -> redirectToActivity(MudcartActivity::class.java)
            "Mud Biking" -> redirectToActivity(MudbikeActivity::class.java)
            "Glass Bridge " -> redirectToActivity(GlassbridgeActivity::class.java)
            "Zip Lining" -> redirectToActivity(ZipliningActivity::class.java)
            "Kayaking " -> redirectToActivity(KayakingActivity::class.java)
            "Sand Biking" -> redirectToActivity(SandbikeActivity::class.java)
            "Sand Boarding" -> redirectToActivity(SandboardActivity::class.java)
            "Rope Cycling " -> redirectToActivity(RopecyclingActivity::class.java)
            "Survival" -> redirectToActivity(SurvivalActivity::class.java)
            "Fishing" -> redirectToActivity(FishingActivity::class.java)
            "Glacial Exploration " -> redirectToActivity(GlacialActivity::class.java)
            "Bunjee Jumping" -> redirectToActivity(BunjeejumpingActivity::class.java)
            "Play Parks" -> redirectToActivity(PlayparksActivity::class.java)
            "Stargazing" -> redirectToActivity(StargazingActivity::class.java)
            "Sandboarding" -> redirectToActivity(SandboardActivity::class.java)
            // Add more cases for other activity types as needed
            else -> {
                // Handle default case when activity type is not recognized
                Log.e("Covers", "Unknown activity type: $activityType")

                // You can display a message to the user or handle it in any way you prefer
            }
        }
    }

    private fun redirectToActivity(activityClass: Class<*>) {
        startActivity(Intent(this@TerrainsActivity, activityClass))
    }
}
