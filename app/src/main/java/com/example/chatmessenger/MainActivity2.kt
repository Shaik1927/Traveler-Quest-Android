package com.example.chatmessenger

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.chatmessenger.activities.SignInActivity
import com.example.chatmessenger.HomeFragment2
import com.example.chatmessenger.databinding.NavHeaderBinding
import com.example.chatmessenger.fragments.SettingFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

// Import your ViewModel here
import com.example.chatmessenger.mvvm.ChatAppViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatmessenger.fragments.HomeFragment

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var viewModel: ChatAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Initialize ViewModel using ViewModelProvider
        viewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)

        // Bind ViewModel to nav_header
        val navHeaderBinding = NavHeaderBinding.bind(navigationView.getHeaderView(0))
        navHeaderBinding.lifecycleOwner = this
        navHeaderBinding.viewModel = viewModel

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment2())
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Load image into ImageView
        val navHeaderImageView = navHeaderBinding.settingUpdateImage
        viewModel.imageUrl.observe(this, { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24) // Placeholder image while loading
                .error(R.drawable.ic_launcher_foreground) // Error image if loading fails
                .into(navHeaderImageView)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment2())
            R.id.nav_profile -> replaceFragment(SettingFragment())
            R.id.nav_search -> replaceFragment(SearchFragment())
            // Add more cases for other navigation items
            R.id.nav_chats -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
//            R.id.nav_chats -> replaceFragment(HomeFragment())
            R.id.nav_logout -> {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
