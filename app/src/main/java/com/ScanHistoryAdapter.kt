package com.khush.devicemapper

import androidx.recyclerview.widget.DiffUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.khush.devicemapper.databinding.ItemScanHistoryBinding
import java.text.DateFormat
import java.util.Date


class ScanHistoryAdapter :
    PagingDataAdapter<ScanRecord, ScanHistoryAdapter.Holder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ScanRecord>() {
            override fun areItemsTheSame(o: ScanRecord, n: ScanRecord) = o.id == n.id
            override fun areContentsTheSame(o: ScanRecord, n: ScanRecord) = o == n
        }
    }

    inner class Holder(val binding: ItemScanHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemScanHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { rec ->
            val tv = holder.binding
            tv.txtType.text = rec.type
            tv.txtId.text = rec.identifier ?: "-"
            tv.txtTime.text = DateFormat.getDateTimeInstance().format(Date(rec.timestamp))
            tv.txtLocation.text = rec.latitude?.let { "${it}, ${rec.longitude}" } ?: "-"
        }
    }
}
