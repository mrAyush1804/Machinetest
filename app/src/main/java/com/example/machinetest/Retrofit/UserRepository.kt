package com.example.machinetest.AdptersAndDataModule

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.machinetest.Retrofit.ApiService
import com.example.machinetest.Retrofit.UserPagingSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getUsers() = Pager(
        config = PagingConfig(
            pageSize = 6, // ReqRes API ke hisaab se
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserPagingSource(apiService) }
    ).flow
}