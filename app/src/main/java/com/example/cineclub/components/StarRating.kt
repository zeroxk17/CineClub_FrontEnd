package com.example.cineclub.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    interactive: Boolean = false,
    starSize: Dp = 28.dp,
    onRatingChange: (Int) -> Unit = {}
) {
    val safeRating = rating.coerceIn(0, 5)
    val accent = MaterialTheme.colorScheme.primary
    val inactive = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (index in 1..5) {
            val active = index <= safeRating
            val starModifier = Modifier
                .size(starSize)
                .let { base ->
                    if (interactive) base.clickable { onRatingChange(index) } else base
                }
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella $index de 5",
                tint = if (active) accent else inactive,
                modifier = starModifier
            )
        }
    }
}
