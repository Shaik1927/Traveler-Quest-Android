package com.simats.chatmessenger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.chatmessenger.databinding.ActivityEnterActivitiesDataBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EnterActivitiesDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterActivitiesDataBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterActivitiesDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Activities")
        storageReference = FirebaseStorage.getInstance().reference.child("Activities_images")

        binding.selectImageButton.setOnClickListener {
            openImageChooser()
        }

        binding.submitButton.setOnClickListener {
            val homeactivity = binding.homeactivity.text.toString()
            val homecategory = binding.homecategory.text.toString()
            val homeurl = binding.homeurl.text.toString()

            if (homeactivity.isNotEmpty() && homecategory.isNotEmpty() && homeurl.isNotEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveData(homeactivity, homecategory, homeurl)
                } else {
                    Toast.makeText(this@EnterActivitiesDataActivity, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@EnterActivitiesDataActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageAndSaveData(activity: String, category: String, url: String) {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val uri = uriTask.result
                            if (uri != null) {
                                saveHomeDataToDatabase(activity, category, url, uri.toString())
                            } else {
                                Toast.makeText(this@EnterActivitiesDataActivity, "Image upload failed: Uri is null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@EnterActivitiesDataActivity, "Image upload failed: ${uriTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@EnterActivitiesDataActivity, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveHomeDataToDatabase(activity: String, category: String, url: String, imageUrl: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val catActData = CatActData(id, activity, url, category, imageUrl)

        if (id != null) {
            databaseReference.child(id).setValue(catActData)
        }

        Toast.makeText(this@EnterActivitiesDataActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
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
