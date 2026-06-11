package com.example.cineclub.navigation

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME_FILTER = "filter"
    const val MOVIE_LIST = "movie_list/{genre}"
    const val MOVIE_DETAIL = "movie_detail/{movieId}"
    const val REVIEW_FORM = "review_form/{movieId}"
    const val MY_REVIEWS = "my_reviews"

    fun createMovieListRoute(genre: String) = "movie_list/$genre"
    fun createMovieDetailRoute(movieId: Int) = "movie_detail/$movieId"
    fun createReviewFormRoute(movieId: Int) = "review_form/$movieId"
}