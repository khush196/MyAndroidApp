package com.khush.devicemapper

// In the same file or a new file
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter as PagingLoadStateAdapter // Alias to avoid name clash if you have your own

class MyPagingLoadStateAdapter(
    private val retry: () -> Unit
) : PagingLoadStateAdapter<DefaultLoadStateViewHolder>() { // Use the ViewHolder here
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = DefaultLoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: DefaultLoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}