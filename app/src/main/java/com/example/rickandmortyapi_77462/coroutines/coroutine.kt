package com.example.rickandmortyapi_77462.coroutines

import kotlinx.coroutines.delay

suspend fun apiReq(){
    println("Making the request")
    delay(2000)
    println("Done")
}