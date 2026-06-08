package com.example.cineclub.models

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("_id") val id: Int,
    @field:SerializedName("usuario") val username: String,
    @field:SerializedName("password") val passwordHash: String?,
    @field:SerializedName("misReseñas") val myReviews: List<Int> = emptyList(),
)