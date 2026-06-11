package com.example.cineclub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cineclub.screens.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SIGNUP) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME_FILTER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Routes.HOME_FILTER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME_FILTER) {
            FilterScreen(
                onGenreSelected = { genre ->
                    navController.navigate(Routes.createMovieListRoute(genre))
                },
                onMovieSelected = { movieId ->
                    navController.navigate(Routes.createMovieDetailRoute(movieId))
                },
                onMyReviews = { navController.navigate(Routes.MY_REVIEWS) }
            )
        }

        composable(Routes.MY_REVIEWS) {
            MyReviewsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.MOVIE_LIST) { backStackEntry ->
            val genre = backStackEntry.arguments?.getString("genre") ?: ""
            MovieListScreen(
                genre = genre,
                onBack = { navController.popBackStack() },
                onMovieSelected = { movieId ->
                    navController.navigate(Routes.createMovieDetailRoute(movieId))
                }
            )
        }

        composable(Routes.MOVIE_DETAIL) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 0
            MovieDetailScreen(
                movieId = movieId,
                onBack = { navController.popBackStack() },
                onWriteReview = { navController.navigate(Routes.createReviewFormRoute(movieId)) }
            )
        }

        composable(Routes.REVIEW_FORM) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 0
            ReviewFormScreen(
                movieId = movieId,
                onCancel = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.popBackStack(Routes.createMovieDetailRoute(movieId), inclusive = false)
                }
            )
        }
    }
}
