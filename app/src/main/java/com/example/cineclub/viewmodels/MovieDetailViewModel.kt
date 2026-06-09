package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import com.example.cineclub.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: Movie, val reviews: List<Review>) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}

class MovieDetailViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun load(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading

            val movieResult = repository.getMovieById(movieId)
            val movie = movieResult.getOrElse {
                _uiState.value = MovieDetailUiState.Error(
                    it.localizedMessage ?: "Error al cargar la película"
                )
                return@launch
            }

            val reviewsResult = repository.getReviewsByMovie(movieId)
            val reviews = reviewsResult.getOrDefault(emptyList())

            _uiState.value = MovieDetailUiState.Success(movie, reviews)
        }
    }
}
