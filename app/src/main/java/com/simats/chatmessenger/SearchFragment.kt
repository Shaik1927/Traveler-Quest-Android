package com.simats.chatmessenger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString().trim()
            var matchFound = false

            if (searchTerm.equals("Trekking", ignoreCase = true) || searchTerm.equals("trekking", ignoreCase = true)) {
                startActivity(Intent(activity, TrekkingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Horse Riding", ignoreCase = true) ||searchTerm.equals("horse riding", ignoreCase = true) ) {
                startActivity(Intent(activity, HorseridingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Paragliding", ignoreCase = true) || searchTerm.equals("paragliding", ignoreCase = true)) {
                startActivity(Intent(activity, ParaglidingActivity::class.java))
                matchFound = true
            }

            if (searchTerm.equals("Hot Air Balloon", ignoreCase = true) || searchTerm.equals("hot air balloon", ignoreCase = true)) {
                startActivity(Intent(activity, HotairballoonActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Wakeboarding", ignoreCase = true) || searchTerm.equals("wakeboarding", ignoreCase = true)) {
                startActivity(Intent(activity, WakeboardingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Bike Trips", ignoreCase = true) || searchTerm.equals("bike trips", ignoreCase = true)) {
                startActivity(Intent(activity, BiketripActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Sky Diving", ignoreCase = true) || searchTerm.equals("sky diving", ignoreCase = true)) {
                startActivity(Intent(activity, SkydivingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Paramotoring", ignoreCase = true) || searchTerm.equals("paramotoring", ignoreCase = true)) {
                startActivity(Intent(activity, ParamotoringActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Scuba Diving", ignoreCase = true) || searchTerm.equals("scuba diving", ignoreCase = true)) {
                startActivity(Intent(activity, ParaglidingActivity::class.java))
                matchFound = true
            }


            if (searchTerm.equals("River Rafting", ignoreCase = true) || searchTerm.equals("river rafting", ignoreCase = true)) {
                startActivity(Intent(activity, RiverraftingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Helicopter Ride", ignoreCase = true) || searchTerm.equals("helicopter ride", ignoreCase = true)) {
                startActivity(Intent(activity, HelicopterActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Safari", ignoreCase = true) || searchTerm.equals("safari", ignoreCase = true)) {
                startActivity(Intent(activity, SafariActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Rock Climbing", ignoreCase = true) || searchTerm.equals("rock climbing", ignoreCase = true)) {
                startActivity(Intent(activity, RockclimbingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Hiking", ignoreCase = true) || searchTerm.equals("hiking", ignoreCase = true)) {
                startActivity(Intent(activity, HikingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Camel Riding", ignoreCase = true) || searchTerm.equals("camel riding", ignoreCase = true)) {
                startActivity(Intent(activity, CamelridingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Boating", ignoreCase = true) || searchTerm.equals("boating", ignoreCase = true)) {
                startActivity(Intent(activity, BoatingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Go Karting", ignoreCase = true) || searchTerm.equals("go karting", ignoreCase = true)) {
                startActivity(Intent(activity, GokartingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Water Parks", ignoreCase = true) || searchTerm.equals("water parks", ignoreCase = true)) {
                startActivity(Intent(activity, WaterparksActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Paint Ball", ignoreCase = true) || searchTerm.equals("paint ball", ignoreCase = true)) {
                startActivity(Intent(activity, PaintballActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Stargazing", ignoreCase = true) ||searchTerm.equals("stargazing", ignoreCase = true) ) {
                startActivity(Intent(activity, StargazingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Camping", ignoreCase = true) || searchTerm.equals("camping", ignoreCase = true) ) {
                startActivity(Intent(activity, CampingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Mountain Biking", ignoreCase = true) || searchTerm.equals("mountain biking", ignoreCase = true)) {
                startActivity(Intent(activity, MountainbikeActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Mountain Carting", ignoreCase = true) || searchTerm.equals("mountain carting", ignoreCase = true)) {
                startActivity(Intent(activity, MountaincartActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Mud Carting", ignoreCase = true) || searchTerm.equals("mud carting", ignoreCase = true)) {
                startActivity(Intent(activity, MudcartActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Mud Biking", ignoreCase = true) || searchTerm.equals("mud biking", ignoreCase = true)) {
                startActivity(Intent(activity, MudbikeActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Glass Bridge", ignoreCase = true) || searchTerm.equals("glass bridge", ignoreCase = true)) {
                startActivity(Intent(activity, GlassbridgeActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Zip Lining", ignoreCase = true) || searchTerm.equals("zip lining", ignoreCase = true)) {
                startActivity(Intent(activity, ZipliningActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Kayaking", ignoreCase = true) || searchTerm.equals("kayaking", ignoreCase = true)) {
                startActivity(Intent(activity, KayakingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Sand Biking", ignoreCase = true) || searchTerm.equals("sand biking", ignoreCase = true)) {
                startActivity(Intent(activity, SandbikeActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Sand Boarding", ignoreCase = true) || searchTerm.equals("sand boarding", ignoreCase = true)) {
                startActivity(Intent(activity, SandboardActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Rope Cycling", ignoreCase = true) || searchTerm.equals("rope cycling", ignoreCase = true)) {
                startActivity(Intent(activity, RopecyclingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Survival", ignoreCase = true) || searchTerm.equals("survival", ignoreCase = true)) {
                startActivity(Intent(activity, SurvivalActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Fishing", ignoreCase = true) || searchTerm.equals("fishing", ignoreCase = true)) {
                startActivity(Intent(activity, FishingActivity::class.java))
                matchFound = true
            }


            if (searchTerm.equals("Glacial Exploration" , ignoreCase = true) || searchTerm.equals("Glacial" , ignoreCase = true)) {
                startActivity(Intent(activity, GlacialActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Bunjee Jumping", ignoreCase = true) || searchTerm.equals("bunjee jumping", ignoreCase = true) || searchTerm.equals("Bunjee", ignoreCase = true)) {
                startActivity(Intent(activity, BunjeejumpingActivity::class.java))
                matchFound = true
            }
            if (searchTerm.equals("Play Parks", ignoreCase = true) || searchTerm.equals("play parks", ignoreCase = true)) {
                startActivity(Intent(activity, PlayparksActivity::class.java))
                matchFound = true
            }
            if(!matchFound) {
                Toast.makeText(requireContext(), "No results found for $searchTerm", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
