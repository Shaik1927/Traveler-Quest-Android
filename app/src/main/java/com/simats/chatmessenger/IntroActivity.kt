package com.simats.chatmessenger


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.simats.chatmessenger.activities.SignInActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)
        val adminButton: Button = findViewById(R.id.button_admin)
        adminButton.setOnClickListener {
            val intent = Intent(this@IntroActivity, AdminLoginActivity::class.java)
            startActivity(intent)
        }

        val userButton: Button = findViewById(R.id.button_user)
        userButton.setOnClickListener {
            val intent = Intent(this@IntroActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
