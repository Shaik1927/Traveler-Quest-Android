package com.simats.chatmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.simats.chatmessenger.databinding.ActivityEnterTopRatedDataBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EnterTopRatedDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterTopRatedDataBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterTopRatedDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Home")
        storageReference = FirebaseStorage.getInstance().reference.child("home_images")

        binding.selectImageButton.setOnClickListener {
            openImageChooser()
        }

        binding.submitButton.setOnClickListener {
            val homeactivity = binding.homeactivity.text.toString()
            val homeurl = binding.homeurl.text.toString()
            val homerating = binding.homerating.text.toString()

            if (homeactivity.isNotEmpty() && homeurl.isNotEmpty() && homerating.isNotEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveData(homeactivity, homeurl, homerating)
                } else {
                    Toast.makeText(this@EnterTopRatedDataActivity, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@EnterTopRatedDataActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageAndSaveData(activity: String, url: String, rating: String) {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val uri = uriTask.result
                            if (uri != null) {
                                saveHomeDataToDatabase(activity, url, rating, uri.toString())
                            } else {
                                Toast.makeText(this@EnterTopRatedDataActivity, "Image upload failed: Uri is null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@EnterTopRatedDataActivity, "Image upload failed: ${uriTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@EnterTopRatedDataActivity, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveHomeDataToDatabase(activity: String, url: String, rating: String, imageUrl: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val homeData = HomeData(id, activity, url, rating, imageUrl)

        if (id != null) {
            databaseReference.child(id).setValue(homeData)
        }

        Toast.makeText(this@EnterTopRatedDataActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
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
