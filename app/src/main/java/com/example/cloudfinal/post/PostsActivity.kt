package com.example.cloudfinal.post


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudfinal.R
import com.example.cloudfinal.adapter.PostAdapter
import com.example.cloudfinal.adapter.UserAdaper
import com.example.cloudfinal.databinding.ActivityChatAppBinding
import com.example.cloudfinal.databinding.ActivityPostsBinding
import com.example.cloudfinal.model.Post
import com.example.cloudfinal.model.User
import com.example.cloudfinal.sign.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostsActivity : AppCompatActivity() {
private lateinit var userRecyclerView: RecyclerView
private lateinit var postsList: ArrayList<Post>
private lateinit var adapter: PostAdapter
private lateinit var binding: ActivityPostsBinding
private lateinit var database: FirebaseDatabase
private lateinit var postsRef: DatabaseReference
private lateinit var auth: FirebaseAuth

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPostsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()
    postsRef = database.reference.child("posts")
    auth = FirebaseAuth.getInstance()
    postsList = ArrayList()
    adapter = PostAdapter(this, postsList)
    userRecyclerView = binding.userRecyclerView
    userRecyclerView.layoutManager = LinearLayoutManager(this)
    userRecyclerView.adapter = adapter


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


                adapter.notifyDataSetChanged()
            }

        override fun onCancelled(error: DatabaseError) {

        }

    })



}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.appbar, menu)
    return super.onCreateOptionsMenu(menu)
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    Firebase.auth.signOut()
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
    return super.onContextItemSelected(item)
}
}