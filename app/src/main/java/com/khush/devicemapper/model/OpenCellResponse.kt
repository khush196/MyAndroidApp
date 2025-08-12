package com.khush.devicemapper.model

data class OpenCellResponse(
    val lat: Double,
    val lon: Double,
    val range: Int?,
    val radio: String?,
    val mcc: Int?,
    val mnc: Int?,
    val cid: Int?,
    val lac: Int?
)
