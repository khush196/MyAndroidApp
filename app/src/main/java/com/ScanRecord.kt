package com.khush.devicemapper

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val name: String?,
    val identifier: String?,
    val timestamp: Long,
    val latitude: Double?,
    val longitude: Double?
)
