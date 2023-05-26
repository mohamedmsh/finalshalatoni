package com.example.cloudfinal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudfinal.R
import com.example.cloudfinal.model.Post
import com.example.cloudfinal.post.CommentsActivity

class PostAdapter (val context: Context, val userList: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PostViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewholder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post, parent, false)
        return PostViewholder(view)
    }

    override fun onBindViewHolder(holder: PostViewholder, position: Int) {
        val currentUser = userList[position]
        holder.title.text = currentUser.title
        holder.content.text = currentUser.content

        holder.itemView.setOnClickListener{
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("postId", currentUser.id)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class PostViewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.cardTitle)
        val content = itemView.findViewById<TextView>(R.id.cardContent)

    }
}
