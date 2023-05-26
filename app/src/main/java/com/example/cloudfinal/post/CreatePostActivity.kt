package com.example.cloudfinal.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cloudfinal.R
import com.example.cloudfinal.databinding.ActivityCreatePostBinding
import com.example.cloudfinal.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreatePostActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var postsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        postsRef = database.reference.child("posts")


        binding.buttonCreatePost.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val content = binding.editTextContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val postId = postsRef.push().key

                val post = Post(postId, title, content, auth.currentUser?.uid)

                if (postId != null) {
                    postsRef.child(postId).setValue(post)
                        .addOnSuccessListener {
                            Toast.makeText(this, "تم إنشاء المنشور بنجاح!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "فشل في إنشاء المنشور!", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "يرجى ملء جميع الحقول", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
