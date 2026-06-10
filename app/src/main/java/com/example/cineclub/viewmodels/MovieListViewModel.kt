package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineclub.models.Movie
import com.example.cineclub.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Success(val movies: List<Movie>) : MovieUiState
    data class Error(val message: String) : MovieUiState
}

class MovieListViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    companion object {
        const val FILTER_ALL = "Todas"
        const val FILTER_FEATURED = "Destacadas"
        private const val FEATURED_LIMIT = 10
    }

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun fetchMovies(genre: String = "") {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading

            val result = when {
                genre.isEmpty() || genre == FILTER_ALL -> repository.getAllMovies()

                genre == FILTER_FEATURED -> repository.getAllMovies().map { all ->
                    all.sortedByDescending { it.metrics.rating }
                        .take(FEATURED_LIMIT)
                }

                else -> repository.getMoviesByGenre(genre)
            }

            result.onSuccess { movies ->
                _uiState.value = MovieUiState.Success(movies)
            }.onFailure { error ->
                _uiState.value = MovieUiState.Error(
                    error.localizedMessage ?: "Error al cargar películas"
                )
            }
        }
    }
}
