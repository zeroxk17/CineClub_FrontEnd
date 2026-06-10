package com.example.cineclub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cineclub.components.MovieRowItem
import com.example.cineclub.viewmodels.MovieListViewModel
import com.example.cineclub.viewmodels.MovieUiState

private data class CategoryInfo(
    val icon: String,
    val title: String,
    val subtitle: String
)

private fun resolveCategory(genre: String): CategoryInfo = when (genre) {
    MovieListViewModel.FILTER_FEATURED -> CategoryInfo("⭐", "Destacadas", "Las películas mejor calificadas")
    MovieListViewModel.FILTER_ALL, "" -> CategoryInfo("🎬", "Todas las películas", "Catálogo completo de CineClub")
    "Drama" -> CategoryInfo("🎭", "Drama", "Historias intensas y emotivas")
    "Acción" -> CategoryInfo("💥", "Acción", "Adrenalina pura en pantalla")
    "Sci-Fi" -> CategoryInfo("🚀", "Sci-Fi", "Mundos imaginarios y futuristas")
    "Comedia" -> CategoryInfo("😂", "Comedia", "Para reír sin parar")
    "Terror" -> CategoryInfo("👻", "Terror", "Que no te dé miedo")
    "Romance" -> CategoryInfo("❤️", "Romance", "Historias de amor")
    else -> CategoryInfo("🎞️", genre, "Películas del género $genre")
}

@Composable
fun MovieListScreen(
    genre: String,
    onBack: () -> Unit,
    onMovieSelected: (Int) -> Unit,
    viewModel: MovieListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val category = resolveCategory(genre)

    LaunchedEffect(genre) {
        viewModel.fetchMovies(genre)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CategoryHeader(
            category = category,
            count = (uiState as? MovieUiState.Success)?.movies?.size,
            onBack = onBack
        )

        when (val state = uiState) {
            is MovieUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is MovieUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "😕",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchMovies(genre) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is MovieUiState.Success -> {
                if (state.movies.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🎬",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No hay películas en esta categoría",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
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
    }
}

@Composable
private fun CategoryHeader(
    category: CategoryInfo,
    count: Int?,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = category.icon,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            val subtitleText = when {
                count == null -> category.subtitle
                count == 1 -> "1 película · ${category.subtitle}"
                else -> "$count películas · ${category.subtitle}"
            }
            Text(
                text = subtitleText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                maxLines = 2
            )
        }
    }
    HeaderDivider()
}

@Composable
private fun HeaderDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    )
}
