package com.simats.chatmessenger
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class PaintballActivity : AppCompatActivity() {

    private lateinit var coversContainer: LinearLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LocationData? = null
    private val locationsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Locations")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paintball)

        coversContainer = findViewById(R.id.coversContainer)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            // Permission already granted, fetch current location
            fetchCurrentLocation()
        }

        // Show all covers
        fetchAndShowCovers()
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    currentLocation = LocationData(latitude = location.latitude, longitude = location.longitude)
                } else {
                    Log.e(TAG, "Current location is null")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error trying to get last known location: ${e.message}")
            }
    }

    private fun fetchAndShowCovers() {
        locationsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Covers", "No data found for covers")
                    return
                }

                for (coversSnapshot in snapshot.children) {
                    val locationData = coversSnapshot.getValue(LocationData::class.java)

                    if (locationData != null) {
                        val imageUrl = locationData.imageUrl

                        if (!imageUrl.isNullOrEmpty() && locationData.activity == "Paint ball" && locationData.myLocation == "Chennai") {
                            val activity = locationData.advLocation
                            val coverItemView: View = layoutInflater.inflate(R.layout.item_location, coversContainer, false)
                            val coverImageView: ImageView = coverItemView.findViewById(R.id.coverImageView)
                            val activityTextView: TextView = coverItemView.findViewById(R.id.activityTextView)

                            // Set data for the ImageView using Picasso
                            Picasso.get().load(imageUrl).into(coverImageView)

                            activityTextView.text = activity ?: locationData.advLocation

                            // Set click listener to open Google Maps with the route
                            coverItemView.setOnClickListener {
                                showRouteToLocation(locationData)
                            }

                            // Add the inflated layout to the linear layout
                            coversContainer.addView(coverItemView)
                        } else {
                            Log.e("Covers", "Image URL is null or empty for an entry")
                        }
                    } else {
                        Log.e("Covers", "Failed to parse LocationData for an entry")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Covers", "Database error: ${error.message}")
            }
        })
    }

    private fun showRouteToLocation(destinationLocation: LocationData) {
        if (currentLocation != null) {
            val uri = "http://maps.google.com/maps?saddr=${currentLocation!!.latitude},${currentLocation!!.longitude}&daddr=${destinationLocation.advLocation}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        } else {
            Log.e(TAG, "Current location is null")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch current location
                fetchCurrentLocation()
            } else {
                Log.e(TAG, "Location permission denied")
            }
        }
    }

    companion object {
        private const val TAG = "TrekkingActivity"
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }
}
