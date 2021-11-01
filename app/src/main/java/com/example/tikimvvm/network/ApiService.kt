package com.example.tikimvvm.network

import com.example.tikimvvm.models.TikiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("shopping-trend/api/trendings/hub")
    suspend fun getAllCategory(
        @Query("cursor") cursor: Int,
        @Query("limit") limit: Int
    ): Response<TikiModel>

    @GET("shopping-trend/api/trendings/hub")
    suspend fun getAllProduct(
        @Query("cursor") cursor: Int,
        @Query("limit") limit: Int
    ): Response<TikiModel>

//    companion object {
//        private const val BASE_URL: String = "https://api.tiki.vn/"
//        private var retrofitService: ApiService? = null
//
//        fun getInstance(): ApiService {
//            if (retrofitService == null) {
//                val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create()).build()
//                retrofitService = retrofit.create(ApiService::class.java)
//            }
//            return retrofitService!!
//        }
//    }
}