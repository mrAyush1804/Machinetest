// UserRepository.kt
package com.example.machinetest.AdptersAndDataModule

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.machinetest.AdptersAndDataModule.data.Data
import com.example.machinetest.AdptersAndDataModule.localstorage.AppDatabase
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.Retrofit.ApiService
import com.example.machinetest.Retrofit.UserPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(private val database: AppDatabase) {

    private val okHttpClient = okhttp3.OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", "reqres-free-v1")
                .build()
            chain.proceed(request)
        }
        .build()


    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(okHttpClient) // 👈 add this line
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getUsers(): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(apiService).apply {
                    onDataLoaded = { users ->
                        // Suspend context mein database operation
                        val entities = users.map {
                            UserEntity(
                                id = it.id,
                                email = it.email,
                                firstName = it.first_name,
                                lastName = it.last_name,
                                avatar = it.avatar
                            )
                        }
                        database.userDao().insertUsers(entities)
                    }
                }
            }
        ).flow
    }

    suspend fun toggleFavorite(user: UserEntity) {
        withContext(Dispatchers.IO) {
            val updatedUser = user.copy(isFavorite = !user.isFavorite)
            database.userDao().updateUser(updatedUser)
        }
    }

    suspend fun getFavoriteUsers(): List<UserEntity> {
        return withContext(Dispatchers.IO) {
            database.userDao().getFavoriteUsers()
        }
    }

    suspend fun getAllUsers(): List<UserEntity> {
        return withContext(Dispatchers.IO) {
            database.userDao().getAllUsers()
        }
    }
}