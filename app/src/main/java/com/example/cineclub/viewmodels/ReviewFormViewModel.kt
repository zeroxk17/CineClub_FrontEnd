package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineclub.models.Movie
import com.example.cineclub.models.Review
import com.example.cineclub.models.ReviewUser
import com.example.cineclub.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

sealed interface ReviewSubmitState {
    data object Idle : ReviewSubmitState
    data object Loading : ReviewSubmitState
    data object Success : ReviewSubmitState
    data class Error(val message: String) : ReviewSubmitState
}

class ReviewFormViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    private val _stars = MutableStateFlow(0)
    val stars: StateFlow<Int> = _stars.asStateFlow()

    private val _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment.asStateFlow()

    private val _submitState = MutableStateFlow<ReviewSubmitState>(ReviewSubmitState.Idle)
    val submitState: StateFlow<ReviewSubmitState> = _submitState.asStateFlow()

    fun loadMovie(movieId: Int) {
        if (_movie.value?.id == movieId) return
        viewModelScope.launch {
            repository.getMovieById(movieId).onSuccess { _movie.value = it }
        }
    }

    fun setStars(value: Int) {
        _stars.value = value.coerceIn(0, 5)
    }

    fun setComment(value: String) {
        _comment.value = value
    }

    fun submit(movieId: Int) {
        if (_stars.value <= 0) {
            _submitState.value = ReviewSubmitState.Error("Selecciona una calificación")
            return
        }
        _submitState.value = ReviewSubmitState.Loading

        val review = Review(
            movieId = movieId,
            stars = _stars.value,
            comment = _comment.value.trim(),
            createdAt = LocalDateTime.now().toString(),
            user = ReviewUser(userId = 1, name = "Derek")
        )

        viewModelScope.launch {
            repository.postReview(review)
                .onSuccess { _submitState.value = ReviewSubmitState.Success }
                .onFailure {
                    _submitState.value = ReviewSubmitState.Error(
                        it.localizedMessage ?: "No se pudo enviar la reseña"
                    )
                }
        }
    }

    fun resetSubmitState() {
        _submitState.value = ReviewSubmitState.Idle
    }
}
