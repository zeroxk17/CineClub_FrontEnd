package com.example.cineclub.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cineclub.components.MovieRowItem
// Importa aquí LoadingScreen y EmptyState cuando Takashi/Derek los suban
// import com.example.cineclub.components.LoadingScreen
// import com.example.cineclub.components.EmptyState
import com.example.cineclub.viewmodels.MovieListViewModel
import com.example.cineclub.viewmodels.MovieUiState

@Composable
fun MovieListScreen(
    genre: String,
    onBack: () -> Unit,
    onMovieSelected: (Int) -> Unit,
    viewModel: MovieListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(genre) {
        viewModel.fetchMovies(genre)
    }

    when (val state = uiState) {
        is MovieUiState.Loading -> {
            // Reemplazar con: LoadingScreen()
        }
        is MovieUiState.Error -> {
            // Reemplazar con: EmptyState(message = state.message, onRetry = { viewModel.fetchMovies(genre) })
        }
        is MovieUiState.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.movies) { movie ->
                    MovieRowItem(
                        movie = movie,
                        onClick = { onMovieSelected(movie.id) }
                    )
                }
            }
        }
    }
}
