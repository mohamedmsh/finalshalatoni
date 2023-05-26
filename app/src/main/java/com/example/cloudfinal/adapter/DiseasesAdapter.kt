package com.example.cloudfinal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudfinal.R
import com.example.cloudfinal.model.Diseases
import com.example.cloudfinal.model.Post
import com.example.cloudfinal.post.CommentsActivity

class DiseasesAdapter (val context: Context, val userList: ArrayList<Diseases>): RecyclerView.Adapter<DiseasesAdapter.DiseasesViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseasesViewholder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.diseases, parent, false)
        return DiseasesViewholder(view)
    }

    override fun onBindViewHolder(holder: DiseasesViewholder, position: Int) {
        val current = userList[position]
        holder.title.text = current.title
        holder.text.text = current.text

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class DiseasesViewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.textMT)
        val text = itemView.findViewById<TextView>(R.id.textM)

    }
}
