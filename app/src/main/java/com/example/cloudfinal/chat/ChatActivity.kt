package com.example.cloudfinal.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudfinal.R
import com.example.cloudfinal.adapter.MessageAdapter
import com.example.cloudfinal.databinding.ActivityChatBinding
import com.example.cloudfinal.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var msgAdaper: MessageAdapter
    private lateinit var msgList: ArrayList<Message>
    private lateinit var database: FirebaseDatabase

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        database = Firebase.database
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val mDbRef = database.reference

        senderRoom = id + senderUid
        receiverRoom = senderUid + id

        supportActionBar?.title = name

        msgList = ArrayList()
        msgAdaper = MessageAdapter(this, msgList)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = msgAdaper


        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    msgList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        msgList.add(message!!)
                    }
                    msgAdaper.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        binding.sendbtn.setOnClickListener{
            val msg = binding.messageBox.text.toString()
            val msgObject = Message(msg, senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(msgObject)
                }
            binding.messageBox.setText("")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}