package com.example.cineclub.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cineclub.components.GenreCard

private data class GenreOption(
    val label: String,
    val icon: String,
    val apiValue: String
)

private val GENRES = listOf(
    GenreOption("Drama", "🎭", "Drama"),
    GenreOption("Acción", "💥", "Acción"),
    GenreOption("Sci-Fi", "🚀", "Sci-Fi"),
    GenreOption("Comedia", "😂", "Comedia"),
    GenreOption("Terror", "👻", "Terror"),
    GenreOption("Romance", "❤️", "Romance"),
)

private const val FEATURED_KEY = "Destacadas"
private const val ALL_KEY = "Todas"

@Composable
fun FilterScreen(
    onGenreSelected: (String) -> Unit,
    @Suppress("UNUSED_PARAMETER") onMovieSelected: (Int) -> Unit,
    onMyReviews: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)) {
                Text(
                    text = "¿Qué quieres ver?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Explora el catálogo de CineClub",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            FeaturedCard(
                title = "Destacadas",
                subtitle = "Las películas mejor calificadas",
                icon = "⭐",
                accentAlpha = 0.55f,
                onClick = { onGenreSelected(FEATURED_KEY) }
            )
        }

        item(span = { GridItemSpan(2) }) {
            FeaturedCard(
                title = "Ver todas",
                subtitle = "Explora el catálogo completo",
                icon = "🎬",
                accentAlpha = 0.35f,
                onClick = { onGenreSelected(ALL_KEY) }
            )
        }

        item(span = { GridItemSpan(2) }) {
            FeaturedCard(
                title = "Mis reseñas",
                subtitle = "Consulta las reseñas que has escrito",
                icon = "📝",
                accentAlpha = 0.45f,
                onClick = onMyReviews
            )
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Géneros",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "· elige uno para filtrar",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                )
            }
        }

        items(GENRES) { genre ->
            GenreCard(
                label = genre.label,
                icon = genre.icon,
                onClick = { onGenreSelected(genre.apiValue) }
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeaturedCard(
    title: String,
    subtitle: String,
    icon: String,
    accentAlpha: Float,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = accentAlpha)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        }
    }
}
