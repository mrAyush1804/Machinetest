package com.example.machinetest.AdptersAndDataModule.Adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.machinetest.AdptersAndDataModule.data.Data
import com.example.machinetest.databinding.ItemuserlistBinding

class UserAdapter : PagingDataAdapter<Data, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(private val binding: ItemuserlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Data?) {
            user ?: return
            try {
                // Bind data to UI
                binding.name.text = "${user.first_name} ${user.last_name}"
                binding.email.text = user.email

                // Load avatar image using Glide
                if (user.avatar.isNotEmpty()) {
                    Glide.with(binding.avatar.context)
                        .load(user.avatar)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .circleCrop()
                        .into(binding.avatar)
                } else {
                    binding.avatar.setImageResource(android.R.drawable.ic_menu_report_image)
                }

                // Favorite button click (placeholder)
                binding.favorite.setOnClickListener {
                    // Add favorite functionality here
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.name.text = "Error"
                binding.email.text = "Unable to load"
                binding.avatar.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemuserlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        try {
            val user = getItem(position)
            holder.bind(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
}