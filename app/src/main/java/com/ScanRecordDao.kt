package com.khush.devicemapper

import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import androidx.paging.PagingSource

@Dao
interface ScanRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: ScanRecord)

    @Query(value = "SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getPagedRecords(): PagingSource<Int, ScanRecord>
}
