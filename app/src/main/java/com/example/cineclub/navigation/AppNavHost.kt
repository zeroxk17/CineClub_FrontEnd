package com.example.cineclub.navigation

// TODO: Sebastián
// AppNavHost(navController): NavHost con composable() por cada Route.
// IMPORTANTE:
//  - En login success: popUpTo(LOGIN) { inclusive = true } para limpiar back stack
//  - Al enviar reseña: popBackStack() hacia DETAIL para no apilar ReviewForm
//  - Pasar argumentos con navArgument() para genre y movieId
