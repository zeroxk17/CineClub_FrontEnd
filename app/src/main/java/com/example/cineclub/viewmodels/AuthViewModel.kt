package com.example.cineclub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineclub.models.User
import com.example.cineclub.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Success(val user: User) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(username: String, password: String) {
        val cleanUser = username.trim()
        if (cleanUser.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Usuario y contraseña son obligatorios")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            repository.getAllUsers()
                .onSuccess { users ->
                    val match = users.firstOrNull {
                        it.username.equals(cleanUser, ignoreCase = true) &&
                            it.passwordHash == password
                    }
                    if (match == null) {
                        _authState.value = AuthState.Error("Usuario o contraseña incorrectos")
                    } else {
                        _currentUser.value = match
                        _authState.value = AuthState.Success(match)
                    }
                }
                .onFailure {
                    _authState.value = AuthState.Error(
                        it.localizedMessage ?: "No se pudo conectar con el servidor"
                    )
                }
        }
    }

    fun signUp(username: String, password: String, confirmPassword: String) {
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

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            // Verificar usuario único
            val existingUsers = repository.getAllUsers().getOrElse {
                _authState.value = AuthState.Error(
                    it.localizedMessage ?: "No se pudo conectar con el servidor"
                )
                return@launch
            }

            if (existingUsers.any { it.username.equals(cleanUser, ignoreCase = true) }) {
                _authState.value = AuthState.Error("Ese usuario ya está registrado")
                return@launch
            }

            // _id requerido por el schema, no es auto-incremental
            val newId = (System.currentTimeMillis() / 1000).toInt()
            val newUser = User(
                id = newId,
                username = cleanUser,
                passwordHash = password,
                myReviews = emptyList()
            )

            repository.createUser(newUser)
                .onSuccess { saved ->
                    _currentUser.value = saved
                    _authState.value = AuthState.Success(saved)
                }
                .onFailure {
                    _authState.value = AuthState.Error(
                        it.localizedMessage ?: "No se pudo crear el usuario"
                    )
                }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
