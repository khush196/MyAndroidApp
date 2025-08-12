package com.khush.devicemapper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn // If you are using cachedIn
import androidx.lifecycle.viewModelScope // If you are using viewModelScope

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).scanRecordDao()

    val pager = Pager(PagingConfig(pageSize = 20)) {
        dao.getPagedRecords()
    }.flow.cachedIn(viewModelScope)
}
