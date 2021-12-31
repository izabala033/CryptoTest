package com.ikersoft.cryptotest.api

import retrofit2.Response
import retrofit2.http.GET

interface Api {

    // GET Crypto list
    @GET("v2/assets")
    suspend fun getCoinList(): Response<Map<String,Any>>

}