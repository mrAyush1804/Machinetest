// UserAdapter.kt
package com.example.machinetest.AdptersAndDataModule.Adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.machinetest.AdptersAndDataModule.data.Data
import com.example.machinetest.AdptersAndDataModule.data.User
import com.example.machinetest.AdptersAndDataModule.localstorage.AppDatabase
import com.example.machinetest.AdptersAndDataModule.localstorage.UserDao
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.R

import com.example.machinetest.databinding.ItemuserlistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserAdapter(
    private val database: AppDatabase,
    private val onFavoriteClick: (Data) -> Unit
) : PagingDataAdapter<Data, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(private val binding: ItemuserlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Data?, database: AppDatabase, onFavoriteClick: (Data) -> Unit) {
            user ?: return
            try {
                binding.name.text = "${user.first_name} ${user.last_name}"
                binding.email.text = user.email

                // Load avatar using Glide
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


                var isFavorite = runBlocking {
                    withContext(Dispatchers.IO) {
                        database.userDao().getUserById(user.id)?.isFavorite ?: false
                    }
                }
                binding.favorite.setImageResource(
                    if (isFavorite) R.drawable.heart else R.drawable.heart_for_at3
                )


                binding.favorite.setOnClickListener {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            val userDao: UserDao = database.userDao()
                            val updatedUser = UserEntity(
                                isFavorite = !isFavorite,
                                id = user.id,
                                email = user.email,
                                firstName = user.first_name,
                                lastName = user.last_name,
                                avatar = user.avatar
                            )
                            userDao.updateUser(updatedUser)
                        }
                        isFavorite = !isFavorite
                        binding.favorite.setImageResource(
                            if (isFavorite) R.drawable.heart_for_at3 else R.drawable.heart
                        )
                        Toast.makeText(binding.root.context, "Favorite $isFavorite", Toast.LENGTH_SHORT).show()
                        onFavoriteClick(user)
                    }
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
            holder.bind(user, database, onFavoriteClick)
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