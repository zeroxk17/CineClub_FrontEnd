package com.example.cineclub.services

import com.example.cineclub.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("users")
    suspend fun getAll(): List<User>

    @POST("users")
    suspend fun create(@Body user: User): User
}
