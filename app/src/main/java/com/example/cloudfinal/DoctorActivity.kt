package com.example.cloudfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cloudfinal.chat.ChatApp_Activity
import com.example.cloudfinal.databinding.ActivityDoctorBinding
import com.example.cloudfinal.post.CreatePostActivity
import com.example.cloudfinal.post.PostsActivity

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chat.setOnClickListener {
            val intent = Intent(this, ChatApp_Activity::class.java)
            startActivity(intent)
        }
        binding.amrad.setOnClickListener {
            val intent = Intent(this, DiseasesActivity::class.java)
            startActivity(intent)
        }
        binding.post.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

    }
}