package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide

class HistoryAdapter(
   /* private val context: Context,
    private val historyList: List<UserProfile>,
    private val onClick: (UserProfile) -> Unit,
    private val onDelete: (UserProfile) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val nameText: TextView = view.findViewById(R.id.profile_name)
        val emailText: TextView = view.findViewById(R.id.profile_email)
        val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)

        fun bind(user: UserProfile) {
            nameText.text = user.name
            emailText.text = user.email

            // Load from URI or placeholder
            if (user.imageUri.isNotEmpty()) {
                Glide.with(context).load(user.imageUri).into(profileImage)
            } else {
                profileImage.setImageResource(R.drawable.default_avatar)
            }

            itemView.setOnClickListener { onClick(user) }
            deleteIcon.setOnClickListener { onDelete(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_history_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position])
    }*/
)
