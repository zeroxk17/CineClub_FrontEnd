package com.example.cineclub.services

import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import com.example.cineclub.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovieApiService {
    @GET("movies")
    suspend fun getAll(): List<Movie>

    @GET("movies")
    suspend fun getByGenre(@Path("genre") genre: String): List<Movie>

    @GET("movies/{id}")
    suspend fun getById(@Path("id") id: Int): Movie

    @POST("reviews")
    suspend fun postReview(@Body review: Review): Review
}