package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineclub.models.Review
import com.example.cineclub.repository.MovieRepository
import com.example.cineclub.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MyReviewItem(
    val review: Review,
    val movieTitle: String
)

sealed interface MyReviewsUiState {
    data object Loading : MyReviewsUiState
    data class Success(val items: List<MyReviewItem>) : MyReviewsUiState
    data class Error(val message: String) : MyReviewsUiState
    data object NoUser : MyReviewsUiState
}

class MyReviewsViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyReviewsUiState>(MyReviewsUiState.Loading)
    val uiState: StateFlow<MyReviewsUiState> = _uiState.asStateFlow()

    fun load() {
        val user = SessionManager.currentUser.value
        if (user == null) {
            _uiState.value = MyReviewsUiState.NoUser
            return
        }

        _uiState.value = MyReviewsUiState.Loading
        viewModelScope.launch {
            val pairsResult = repository.getMyReviews(user.id)
            val pairs = pairsResult.getOrElse {
                _uiState.value = MyReviewsUiState.Error(
                    it.localizedMessage ?: "No se pudieron cargar tus reseñas"
                )
                return@launch
            }

            val items = pairs
                .sortedByDescending { it.second.createdAt }
                .map { (movie, review) ->
                    MyReviewItem(review = review, movieTitle = movie.title)
                }

            _uiState.value = MyReviewsUiState.Success(items)
        }
    }
}
