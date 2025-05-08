package com.example.machinetest.AdptersAndDataModule.data

data class User(
    val `data`: List<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)