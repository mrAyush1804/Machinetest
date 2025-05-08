// FavoriteUserAdapter.kt
package com.example.machinetest.AdptersAndDataModule.Adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.R
import com.example.machinetest.databinding.ItemuserlistBinding

class FavoriteUserAdapter(
    private val onFavoriteClick: (UserEntity) -> Unit
) : ListAdapter<UserEntity, FavoriteUserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(private val binding: ItemuserlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity, onFavoriteClick: (UserEntity) -> Unit) {
            try {
                binding.name.text = "${user.firstName} ${user.lastName}"
                binding.email.text = user.email

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

                binding.favorite.setImageResource(
                    if (user.isFavorite) R.drawable.heart else R.drawable.heart_for_at3
                )

                binding.favorite.setOnClickListener {
                    onFavoriteClick(user)
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
        holder.bind(getItem(position), onFavoriteClick)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}