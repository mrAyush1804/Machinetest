package com.example.machinetest.AdptersAndDataModule.Adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.databinding.ItemuserlistBinding

class UnFavoriteAdapter(
    private val userList: List<UserEntity>,
    private val onClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<UnFavoriteAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemuserlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemuserlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.apply {
            name.text = user.firstName
            email.text = user.email

            if (user.avatar.isNotEmpty()) {
                Glide.with(avatar.context)
                    .load(user.avatar)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .circleCrop()
                    .into(avatar)
            } else {
                avatar.setImageResource(android.R.drawable.ic_menu_report_image)
            }

            favorite.setOnClickListener {
                onClick(user)
            }
        }
    }

    override fun getItemCount(): Int = userList.size
}