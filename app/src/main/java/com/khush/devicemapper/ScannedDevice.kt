package com.khush.devicemapper

data class ScannedDevice(
    val type: String,
    val name: String,
    val macAddress: String,
    val timestamp: Long? = System.currentTimeMillis(),
    val identifier: String,
    val signalStrength: Long?,
    var latitude: Double? = null,
    var longitude: Double? = null
)