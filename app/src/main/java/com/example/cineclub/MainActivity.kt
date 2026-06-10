package com.example.cineclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import com.example.cineclub.navigation.AppNavHost
import com.example.cineclub.ui.theme.CineClubTheme
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCoilImageLoader()
        enableEdgeToEdge()
        setContent {
            CineClubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Wikimedia bloquea el User-Agent default de OkHttp; usamos uno custom
    private fun setupCoilImageLoader() {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "CineClub/1.0 (Android app; educational project)"
                    )
                    .build()
                chain.proceed(request)
            }
            .build()

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .okHttpClient(okHttpClient)
                .crossfade(true)
                .build()
        )
    }
}
