package com.example.rickandmortyapi_77462.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rickandmortyapi_77462.models.Character
import com.example.rickandmortyapi_77462.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(innerPadding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var data by remember { mutableStateOf(listOf<com.example.rickandmortyapi_77462.models.Character>()) }
    var isLoading by remember { mutableStateOf(false) }

//    API CALL - HANDLING ERRORS
    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                isLoading = true
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val characterService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CharacterService::class.java)

                val res = characterService.getCharacters()
                data = res.results
                Log.i("Res", data.toString())
                isLoading = false
            } catch (e: Exception) {
                data = listOf()
                Log.i("ErrorScreen", e.toString())
                isLoading = false
            }
        }
    }
// MANAGING IS LOADING STATE AND INFO RECIVED
    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) { character ->
                CharacterCard(
                    character = character,
                    onCharacterClick = {
                        navController.navigate("character/${character.id}") }
                )
            }
        }
    }
}
@Composable
fun CharacterCard(
    character: Character,
    onCharacterClick: () -> Unit

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCharacterClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = character.species,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}