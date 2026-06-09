package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cineclub.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Success(val user: User) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel : ViewModel() {

    private val users: MutableList<User> = mutableListOf(
        User(id = 1, username = "derek", passwordHash = "1234", myReviews = emptyList())
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(username: String, password: String) {
        _authState.value = AuthState.Loading

        val cleanUser = username.trim()
        if (cleanUser.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Usuario y contraseña son obligatorios")
            return
        }

        val match = users.firstOrNull {
            it.username.equals(cleanUser, ignoreCase = true) && it.passwordHash == password
        }

        if (match == null) {
            _authState.value = AuthState.Error("Usuario o contraseña incorrectos")
            return
        }

        _currentUser.value = match
        _authState.value = AuthState.Success(match)
    }

    fun signUp(username: String, password: String, confirmPassword: String) {
        _authState.value = AuthState.Loading

        val cleanUser = username.trim()

        if (cleanUser.isEmpty()) {
            _authState.value = AuthState.Error("El usuario no puede estar vacío")
            return
        }
        if (password.length < 4) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 4 caracteres")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        if (users.any { it.username.equals(cleanUser, ignoreCase = true) }) {
            _authState.value = AuthState.Error("Ese usuario ya está registrado")
            return
        }

        val newUser = User(
            id = (users.maxOfOrNull { it.id } ?: 0) + 1,
            username = cleanUser,
            passwordHash = password,
            myReviews = emptyList()
        )
        users.add(newUser)
        _currentUser.value = newUser
        _authState.value = AuthState.Success(newUser)
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
