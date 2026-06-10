package com.example.cineclub.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://four11-cineclub-backend.onrender.com/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val movieService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
    val userService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }

    // Alias para compatibilidad con código previo
    val instance: MovieApiService get() = movieService
}
