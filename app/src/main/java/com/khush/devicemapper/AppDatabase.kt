package com.khush.devicemapper

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room


@Database(entities = [ScanRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanRecordDao(): ScanRecordDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "scan-db")
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
