package com.khush.devicemapper.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.khush.devicemapper.model.OpenCellResponse

interface OpenCelliDApi {
    @GET("cell/get")
    suspend fun getCellInfo(
        @Query("key") apiKey: String,
        @Query("radio") radio: String,
        @Query("mcc") mcc: Int,
        @Query("mnc") mnc: Int,
        @Query("lac") lac: Int,
        @Query("cid") cid: Int,
        @Query("format") format: String = "json"
    ): Response<OpenCellResponse>
}
