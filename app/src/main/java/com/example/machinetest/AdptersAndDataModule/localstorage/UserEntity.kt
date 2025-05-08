package com.example.machinetest.AdptersAndDataModule.localstorage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val isFavorite: Boolean = false
)
