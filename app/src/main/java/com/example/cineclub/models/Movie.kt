package com.example.cineclub.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @field:SerializedName("_id") val id: Int,
    @field:SerializedName("titulo") val title: String,
    @field:SerializedName("sinopsis") val overview: String,
    @field:SerializedName("añoLanzamiento") val year: Int,
    @field:SerializedName("duracion") val durationMin: Int,
    @field:SerializedName("clasificacion") val ratingClass: String,
    @field:SerializedName("generos") val genres: List<String>,
    @field:SerializedName("director") val director: String,
    @field:SerializedName("reparto") val cast: List<String>,
    @field:SerializedName("posterUrl") val posterUrl: String,
    @field:SerializedName("backdropUrl") val backdropUrl: String,
    @field:SerializedName("metricas") val metrics: Metrics,
)

data class Metrics(
    @field:SerializedName("calificacionPromedio") val rating: Double = 0.0,
    @field:SerializedName("totalResenas") val reviewsCount: Int = 0,
)