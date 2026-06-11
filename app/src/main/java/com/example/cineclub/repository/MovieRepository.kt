package com.example.cineclub.repository

import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import com.example.cineclub.services.RetrofitClient

class MovieRepository {
    private val apiService = RetrofitClient.movieService

    suspend fun getAllMovies(): Result<List<Movie>> {
        return try {
            Result.success(apiService.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMoviesByGenre(genre: String): Result<List<Movie>> {
        return try {
            // Filtro en cliente: el backend no tiene query param de género
            val allMovies = apiService.getAll()
            val filtered = allMovies.filter { it.genres.contains(genre) }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieById(id: Int): Result<Movie> {
        return try {
            // Filtro en cliente: el backend no expone GET /movies/{id}
            val all = apiService.getAll()
            val movie = all.firstOrNull { it.id == id }
                ?: return Result.failure(NoSuchElementException("Película no encontrada"))
            Result.success(movie)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByMovie(movieId: Int): Result<List<Review>> {
        return try {
            Result.success(apiService.getReviewsByMovie(movieId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUser(userId: Int): Result<List<Review>> {
        return try {
            Result.success(apiService.getReviewsByUser(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * El backend aún no expone GET /reviews/usuario/{id} en producción (404).
     * Como alternativa, se obtienen las reseñas de todas las películas y se
     * filtran en cliente por el id del usuario.
     */
    suspend fun getMyReviews(userId: Int): Result<List<Pair<Movie, Review>>> {
        return try {
            val movies = apiService.getAll()
            val result = mutableListOf<Pair<Movie, Review>>()
            for (movie in movies) {
                val reviews = try {
                    apiService.getReviewsByMovie(movie.id)
                } catch (e: Exception) {
                    emptyList()
                }
                reviews.filter { it.user.userId == userId }
                    .forEach { result.add(movie to it) }
            }
            Result.success(result)
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
