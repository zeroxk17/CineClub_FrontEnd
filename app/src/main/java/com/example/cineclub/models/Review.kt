package com.example.cineclub.models

import com.google.gson.annotations.SerializedName

data class Review(
    @field:SerializedName("_id") val id: Int = 0,
    @field:SerializedName("peliculaId") val movieId: Int,
    @field:SerializedName("calificacion") val stars: Int,
    @field:SerializedName("comentario") val comment: String,
    @field:SerializedName("fechaPublicacion") val createdAt: String,
    @field:SerializedName("usuario") val user: ReviewUser,
)

data class ReviewUser(
    @field:SerializedName("usuarioId") val userId: Int,
    @field:SerializedName("nombre") val name: String,
)