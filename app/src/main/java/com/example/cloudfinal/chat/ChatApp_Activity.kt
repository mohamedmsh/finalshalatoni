package com.example.cloudfinal.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudfinal.R
import com.example.cloudfinal.adapter.UserAdaper
import com.example.cloudfinal.databinding.ActivityChatAppBinding
import com.example.cloudfinal.model.User
import com.example.cloudfinal.sign.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatApp_Activity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdaper
    private lateinit var binding: ActivityChatAppBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        database = Firebase.database
        super.onCreate(savedInstanceState)
        binding = ActivityChatAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mDbRef = database.reference

        userList = ArrayList()
        adapter = UserAdaper(this, userList)
        userRecyclerView = binding.userRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter


        mDbRef.child("users").addValueEventListener(object: ValueEventListener {
            var username ="username"
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    val userRef = database.reference.child("users").child(postSnapshot.key!!)
                    userRef.child("userName").get().addOnSuccessListener { name ->
                        Log.e("hshm", name.value.toString())
                        username =name.value.toString()
                    }
                    currentUser?.username.equals(username)
                    Log.e("hshm",username)

                 //   if (Firebase.auth.currentUser!!.uid != currentUser?.uid){
                        userList.add(User(username,null,postSnapshot.key!!))
                  //  }
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