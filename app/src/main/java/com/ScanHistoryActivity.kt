package com.khush.devicemapper

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import androidx.paging.LoadStateAdapter

class ScanHistoryActivity : AppCompatActivity() {
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)
        supportActionBar?.title = "Scan History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recycler = findViewById<RecyclerView>(R.id.recyclerHistory)
        val adapter = ScanHistoryAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MyPagingLoadStateAdapter { adapter.retry() },
            footer = MyPagingLoadStateAdapter { adapter.retry() }
        )

        lifecycleScope.launch {
            viewModel.pager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
