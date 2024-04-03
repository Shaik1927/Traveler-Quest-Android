package com.example.chatmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import android.location.Geocoder
import android.location.Address


import com.google.firebase.database.*
import java.util.*

class HomeFragment2 : Fragment(R.layout.fragment_home2), View.OnClickListener {

    private lateinit var coversContainer: LinearLayout
    private lateinit var locationEditText: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val homeReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Home")
    private val otherTableReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("OtherTable")

    private val sectionIds = intArrayOf(
        R.id.mountain, R.id.hills, R.id.forests, R.id.desert, R.id.canyons, R.id.marine,
        R.id.swamp, R.id.seas, R.id.terrains, R.id.ponds, R.id.rivers, R.id.amusement,
        R.id.land, R.id.water, R.id.air
    )

    private companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coversContainer = view.findViewById(R.id.coversContainer)
        locationEditText = view.findViewById(R.id.locationEditText)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set click listeners
        sectionIds.forEach { view.findViewById<View>(it).setOnClickListener(this) }
        view.findViewById<Button>(R.id.findLocationButton).setOnClickListener { getLocation() }

        // Show all covers
        showCovers()
    }

    override fun onClick(view: View) {
        when (view.id) {
            in sectionIds -> redirectToActivity(getSectionActivity(view.id))
        }
    }

    private fun getSectionActivity(viewId: Int): Class<*> {
        return when (viewId) {
            R.id.mountain -> MountainActivity::class.java
            R.id.hills -> HillsActivity::class.java
            R.id.forests -> ForestsActivity::class.java
            R.id.desert -> DesertActivity::class.java
            R.id.canyons -> CanyonsActivity::class.java
            R.id.marine -> MarineActivity::class.java
            R.id.swamp -> SwampsActivity::class.java
            R.id.seas -> SeasActivity::class.java
            R.id.terrains -> TerrainsActivity::class.java
            R.id.ponds -> PondsActivity::class.java
            R.id.rivers -> RiversActivity::class.java
            R.id.amusement -> AmusementActivity::class.java
            R.id.land -> LandadvActivity::class.java
            R.id.water -> WateradvActivity::class.java
            R.id.air -> AiradvActivity::class.java
            else -> throw IllegalArgumentException("Unknown view ID: $viewId")
        }
    }

    private fun showCovers() {
        homeReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Covers", "No data found for covers")
                    return
                }

                for (coversSnapshot in snapshot.children) {
                    val homeData = coversSnapshot.getValue(HomeData::class.java)

                    if (homeData != null) {
                        val imageUrl = homeData.imageUrl

                        if (!imageUrl.isNullOrEmpty()) {
                            val url = snapshot.child("url").getValue(String::class.java)
//                            val rating = snapshot.child("rating").getValue(String::class.java)

                            val activity = homeData.activty
                            val coverItemView: View = layoutInflater.inflate(R.layout.item_home_data, coversContainer, false)
                            val coverImageView: ImageView = coverItemView.findViewById(R.id.coverImageView)
                            val activityTextView: TextView = coverItemView.findViewById(R.id.activityTextView)
//                            val ratingBar: RatingBar = coverItemView.findViewById(R.id.ratingBar)

                            // Set data for the ImageView using Picasso
                            Picasso.get().load(imageUrl).into(coverImageView)

                            activityTextView.text = activity ?: homeData.activty

//                            ratingBar.rating = rating?.toFloatOrNull() ?: homeData.rating?.toFloatOrNull() ?: 0f
                            // Fetch additional data from OtherTable
                            val coverId = homeData.id // Assuming each cover has a unique identifier
                            fetchOtherTableData(coverId, coverItemView, homeData)

                            // Add click listener to each cover container
                            coverItemView.setOnClickListener {
                                handleCoverContainerClick(activity)
                            }

                            // Add the inflated layout to the linear layout
                            coversContainer.addView(coverItemView)
                        } else {
                            Log.e("Covers", "Image URL is null or empty for an entry")
                        }
                    } else {
                        Log.e("Covers", "Failed to parse HomeData for an entry")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Covers", "Database error: ${error.message}")
            }
        })
    }

    private fun fetchOtherTableData(coverId: String?, coverItemView: View, homeData: HomeData) {
        otherTableReference.child(coverId.orEmpty()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve data from snapshot and update UI accordingly
                    val activityTextView: TextView = coverItemView.findViewById(R.id.activityTextView)

//                    val ratingBar: RatingBar = coverItemView.findViewById(R.id.ratingBar)

                    val activity = snapshot.child("activty").getValue(String::class.java)
                    val url = snapshot.child("url").getValue(String::class.java)
//                    val rating = snapshot.child("rating").getValue(String::class.java)

                    // Update UI with fetched data
                    activityTextView.text = activity ?: homeData.activty

//                    ratingBar.rating = rating?.toFloatOrNull() ?: homeData.rating?.toFloatOrNull() ?: 0f
                } else {
                    Log.e("Covers", "No data found for OtherTable entry with coverId: $coverId")

                    // Handle the case where data is not fetched from OtherTable
                    // You can update UI or log a message as needed
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Covers", "Database error: ${error.message}")
            }
        })
    }

    private fun handleCoverContainerClick(activityType: String?) {
        when (activityType) {
            "Trekking" -> redirectToActivity(TrekkingActivity::class.java)
            "Horse Riding " -> redirectToActivity(HorseridingActivity::class.java)
            "Paragliding" -> redirectToActivity(ParaglidingActivity::class.java)
            "Hot Air Balloon" -> redirectToActivity(HotairballoonActivity::class.java)
            "Wakeboarding " -> redirectToActivity(WakeboardingActivity::class.java)
            "Bike Trips" -> redirectToActivity(BiketripActivity::class.java)
            "Sky Diving" -> redirectToActivity(SkydivingActivity::class.java)
            "Paramotoring" -> redirectToActivity(ParamotoringActivity::class.java)
            "Scuba Diving" -> redirectToActivity(ScubadivingActivity::class.java)
            "Surfing" -> redirectToActivity(SurfingActivity::class.java)
            "Kayaking" -> redirectToActivity(KayakingActivity::class.java)
            // Add more cases for other activity types as needed
            else -> {
                // Handle default case when activity type is not recognized
                Log.e("Covers", "Unknown activity type: $activityType")

                // You can display a message to the user or handle it in any way you prefer
            }
        }
    }

    private fun redirectToActivity(activityClass: Class<*>) {
        startActivity(Intent(context, activityClass))
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses: List<Address>? = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        addresses?.let { addressList ->
                            if (addressList.isNotEmpty()) {
                                val cityName = addressList[0].locality
                                locationEditText.setText(cityName)
                            } else {
                                Log.e("HomeFragment2", "No city or town found for the location")
                                locationEditText.setText("City or town not found")
                            }
                        } ?: run {
                            Log.e("HomeFragment2", "No address found for the location")
                            locationEditText.setText("Address not found")
                        }
                    } ?: run {
                        Log.e("HomeFragment2", "Location not available")
                        locationEditText.setText("Location not available")
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Log.e("HomeFragment2", "Location permission denied")
                locationEditText.setText("Location permission denied")
            }
        }
    }
}
