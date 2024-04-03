package com.example.chatmessenger


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.LinearLayout


class AppActivitiesFragment : Fragment() {

    private lateinit var topRateLayout: LinearLayout
    private lateinit var activitiesLayout: LinearLayout
    private lateinit var locationsLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_app_activities, container, false)

        topRateLayout = view.findViewById(R.id.add_top_rate)
        activitiesLayout = view.findViewById(R.id.add_activity)
        locationsLayout = view.findViewById(R.id.add_location)

        topRateLayout.setOnClickListener {
            // Redirect to EnterTopRatedDataActivity
            val intent = Intent(requireActivity(), EnterTopRatedDataActivity::class.java)
            startActivity(intent)
        }

        activitiesLayout.setOnClickListener {
            // Redirect to EnterActivitiesDataActivity
            val intent = Intent(requireActivity(), EnterActivitiesDataActivity::class.java)
            startActivity(intent)
        }

        locationsLayout.setOnClickListener {
            // Redirect to EnterLocationDataActivity
            val intent = Intent(requireActivity(), EnterLocationsDataActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}