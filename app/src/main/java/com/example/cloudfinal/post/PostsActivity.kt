package com.example.cloudfinal.post


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.cloudfinal.databinding.ActivityPostsBinding
import com.example.cloudfinal.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class PostsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var postsRef: DatabaseReference
    private lateinit var commentsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        postsRef = database.reference.child("posts")
        commentsRef = database.reference.child("comments")

        val postsList = mutableListOf<Post>()
        val postsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postsList)
        binding.listViewPosts.adapter = postsAdapter

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postsList.clear()

                val posts = dataSnapshot.getValue(object : GenericTypeIndicator<HashMap<String, Any>>() {})
                posts?.let {
                    for ((key, value) in it) {
                        val userDetails = value as HashMap<String, Any>
                        val title = userDetails["title"].toString()
                        val authorId = userDetails["authorId"].toString()
                        val id = userDetails["id"].toString()
                        val content = userDetails["content"].toString()
                        postsList.add(Post(id, title, content, authorId))


                    }
                }


                postsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@PostsActivity, "حدث خطأ أثناء جلب المنشورات!", Toast.LENGTH_SHORT).show()
            }
        })

        binding.listViewPosts.setOnItemClickListener { _, _, position, _ ->
            val post = postsList[position]

            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("postId", post.id)
            startActivity(intent)
        }
    }
}
