package com.example.rickandmortyapi_77462.services
import com.example.rickandmortyapi_77462.models.Character
import com.example.rickandmortyapi_77462.models.CharacterRes
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("character")
    suspend fun getCharacters(): CharacterRes

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character

}