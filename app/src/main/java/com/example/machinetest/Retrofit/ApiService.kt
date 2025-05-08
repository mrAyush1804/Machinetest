package com.example.machinetest.Retrofit
import com.example.machinetest.AdptersAndDataModule.data.User
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): User
}