package com.example.cineclub.repository

import com.example.cineclub.models.User
import com.example.cineclub.services.RetrofitClient

class UserRepository {
    private val apiService = RetrofitClient.userService

    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            Result.success(apiService.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: User): Result<User> {
        return try {
            Result.success(apiService.create(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
