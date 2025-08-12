package com.khush.devicemapper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView

class DefaultLoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false)
) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
    private val retryButton: Button = itemView.findViewById<Button>(R.id.retry_button)
        .also { button ->
        button.setOnClickListener {  _: View -> retry() }
        }


    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.Loading -> {
                progressBar.isVisible = true
                errorMsg.isVisible = false
                retryButton.isVisible = false
            }
            is LoadState.Error -> {
                progressBar.isVisible = false
                retryButton.isVisible = true
                // Safely access the message and then check if it's null or blank
                val errorMessage = loadState.error.message
                errorMsg.isVisible = !errorMessage.isNullOrBlank()
                errorMsg.text = errorMessage ?: "Unknown error" // Provide a default if null
            }
            is LoadState.NotLoading -> {
                progressBar.isVisible = false
                errorMsg.isVisible = false
                retryButton.isVisible = false
            }
        }
    }
}
