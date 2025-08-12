package com.khush.devicemapper.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: OpenCelliDApi by lazy {
     val retrofit = Retrofit.Builder()
            .baseUrl("https://opencellid.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
          retrofit.create(OpenCelliDApi::class.java)
    }
}
