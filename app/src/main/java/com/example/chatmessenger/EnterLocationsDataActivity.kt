package com.example.chatmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri

import android.provider.MediaStore
import android.widget.Toast

import com.example.chatmessenger.databinding.ActivityEnterLocationsDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EnterLocationsDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterLocationsDataBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterLocationsDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Locations") // Use "Locations" instead of "Activities"
        storageReference = FirebaseStorage.getInstance().reference.child("Locations_images") // Use "Locations_images" instead of "Activities_images"

        binding.selectImageButton.setOnClickListener {
            openImageChooser()
        }

        binding.submitButton.setOnClickListener {
            val activity = binding.activity.text.toString() // Change to the appropriate view ID
            val myLocation = binding.myLocation.text.toString() // Change to the appropriate view ID
            val advLocation = binding.advLocation.text.toString() // Change to the appropriate view ID

            if (activity.isNotEmpty() && myLocation.isNotEmpty() && advLocation.isNotEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveData(activity, myLocation, advLocation)
                } else {
                    Toast.makeText(this@EnterLocationsDataActivity, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@EnterLocationsDataActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageAndSaveData(activity: String, myLocation: String, advLocation: String) {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val uri = uriTask.result
                            if (uri != null) {
                                saveDataToDatabase(activity, myLocation, advLocation, uri.toString())
                            } else {
                                Toast.makeText(this@EnterLocationsDataActivity, "Image upload failed: Uri is null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@EnterLocationsDataActivity, "Image upload failed: ${uriTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@EnterLocationsDataActivity, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveDataToDatabase(activity: String, myLocation: String, advLocation: String, imageUrl: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val locationData = LocationData(id, activity, myLocation, advLocation, imageUrl)

        if (id != null) {
            databaseReference.child(id).setValue(locationData)
        }

        Toast.makeText(this@EnterLocationsDataActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
        }
    }
}
