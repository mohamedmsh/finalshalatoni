package com.example.cloudfinal.post


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.cloudfinal.databinding.ActivityCommentsBinding
import com.example.cloudfinal.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var commentsRef: DatabaseReference
    private lateinit var postId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        commentsRef = database.reference.child("comments")

        postId = intent.getStringExtra("postId") ?: ""


        val commentsList = mutableListOf<Comment>()

        val commentsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, commentsList)
        binding.listViewComments.adapter = commentsAdapter

        commentsRef.orderByChild("postId").equalTo(postId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    commentsList.clear()

                    for (commentSnapshot in dataSnapshot.children) {
                        val comment = commentSnapshot.getValue(Comment::class.java)
                        if (comment != null) {
                            commentsList.add(comment)
                        }
                    }

                    commentsAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@CommentsActivity, "حدث خطأ أثناء جلب التعليقات!", Toast.LENGTH_SHORT).show()
                }
            })

        binding.buttonAddComment.setOnClickListener {
            val content = binding.editTextComment.text.toString().trim()

            if (content.isNotEmpty()) {
                val commentId = commentsRef.push().key
                val comment = Comment(commentId, postId, content, auth.currentUser?.uid)
                if (commentId != null) {
                    commentsRef.child(commentId).setValue(comment)
                        .addOnSuccessListener {
                            Toast.makeText(this, "تم إضافة التعليق بنجاح!", Toast.LENGTH_SHORT).show()
                            binding.editTextComment.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "فشل في إضافة التعليق!", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "يرجى إدخال التعليق", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
