package com.example.cineclub.services

import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovieApiService {
    @GET("movies")
    suspend fun getAll(): List<Movie>

    // El backend no tiene GET /movies/{id} asi que se filtra en cliente.

    @GET("reviews/pelicula/{peliculaId}")
    suspend fun getReviewsByMovie(@Path("peliculaId") movieId: Int): List<Review>

    @GET("reviews/usuario/{usuarioId}")
    suspend fun getReviewsByUser(@Path("usuarioId") userId: Int): List<Review>

    @POST("reviews")
    suspend fun postReview(@Body review: Review): Review
}
