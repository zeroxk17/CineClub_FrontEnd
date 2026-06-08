package com.example.cineclub.repository

import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import com.example.cineclub.services.RetrofitClient

class MovieRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllMovies(): Result<List<Movie>> {
        return try {
            Result.success(apiService.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMoviesByGenre(genre: String): Result<List<Movie>> {
        return try {
            // Filtrado básico asumiendo que el backend retorna todo en /movies
            // Si el backend tiene endpoint específico, se cambia la llamada aquí.
            val allMovies = apiService.getAll()
            val filtered = allMovies.filter { it.genres.contains(genre) }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun postReview(review: Review): Result<Review> {
        return try {
            Result.success(apiService.postReview(review))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}