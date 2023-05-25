package com.example.cloudfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cloudfinal.chat.ChatApp_Activity

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val intent = Intent(this, ChatApp_Activity::class.java)
        startActivity(intent)
    }
}