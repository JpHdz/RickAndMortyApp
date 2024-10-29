package com.example.rickandmortyapi_77462

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapi_77462.ui.theme.RickAndMortyAPI_77462Theme
import com.example.rickandmortyapi_77462.ui.theme.screens.DetailCharacterScreen
import com.example.rickandmortyapi_77462.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyAPI_77462Theme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "home"){
                        composable(route = "home"){
                            HomeScreen(innerPadding,navController)
                        }
                        composable("character/{id}"){ navBackStackEntry ->
                            val characterId = navBackStackEntry.arguments?.getString("id")
                            DetailCharacterScreen(characterId = characterId,innerPadding,navController)
                        }
                    }
                }
            }
        }
    }
}

