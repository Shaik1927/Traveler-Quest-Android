package com.example.chatmessenger


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityAdminLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("admin")

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.loginemail.text.toString().trim()
            val loginPassword = binding.loginpassword.text.toString().trim()

            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginUser(loginEmail, loginPassword)
            } else {
                Toast.makeText(
                    this@AdminLoginActivity,
                    "All Fields are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val adminData = userSnapshot.getValue(AdminData::class.java)

                            if (adminData != null && adminData.password == password) {
                                Toast.makeText(
                                    this@AdminLoginActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@AdminLoginActivity, AdminMainActivity::class.java)
                                startActivity(intent)
                                finish()
                                return
                            }
                        }
                        Toast.makeText(
                            this@AdminLoginActivity,
                            "Login Failed: Incorrect password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@AdminLoginActivity,
                            "Login Failed: User not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@AdminLoginActivity,
                        "Database Error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}