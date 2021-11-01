package com.example.tikimvvm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient private constructor() {
    companion object {
        private const val BASE_URL = "https://api.tiki.vn"
        private lateinit var apiService: ApiService
        private var instance: RestClient? = null

        fun getInstance(): RestClient {
            if (instance == null) {
                synchronized(this) {
                    instance = RestClient()
                }
            }
            return instance!!
        }
    }

    init {
        val retroFit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retroFit.create(ApiService::class.java)
    }

    fun getApiService() = apiService
}