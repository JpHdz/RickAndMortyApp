package com.example.rickandmortyapi_77462.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rickandmortyapi_77462.models.Character
import com.example.rickandmortyapi_77462.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun DetailCharacterScreen(characterId: String?, innerPadding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var data by remember { mutableStateOf<Character?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

//    API CALL AND HANDLING ERROR
    LaunchedEffect(characterId) {
        scope.launch {
            try {
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val characterService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CharacterService::class.java)

                data = characterId?.toInt()?.let { characterService.getCharacterById(it) }
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "ERROR GETTING DATA"
                isLoading = false
                Log.e("CharacterDetailScreen", e.toString())
            }
        }
    }

//    SETTING VIEW DEPENDING ON STATE
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Text(text = errorMessage ?: "ERROR GETTING THE INFO", color = Color.Red)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                data?.let { character ->
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailItem("Status", character.status)
                            DetailItem("Species", character.species)
                            DetailItem("Gender", character.gender)
                            DetailItem("Origin", character.origin.name)
                            DetailItem("Location", character.location.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}