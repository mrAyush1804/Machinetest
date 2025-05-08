package com.example.machinetest.AdptersAndDataModule

import com.example.machinetest.AdptersAndDataModule.data.Data
import com.example.machinetest.AdptersAndDataModule.data.Support

data class User(
    val `data`: List<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)