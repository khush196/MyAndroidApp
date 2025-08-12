package com.khush.devicemapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.khush.devicemapper.ScannedDevice
import com.khush.devicemapper.R
import android.widget.ImageView
import android.view.View
import android.widget.TextView
import android.view.ViewGroup

class ScannedDeviceAdapter(private val devices: List<ScannedDevice>) :
    RecyclerView.Adapter<ScannedDeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.device_icon)
        val name: TextView = itemView.findViewById(R.id.device_name)
        val type: TextView = itemView.findViewById(R.id.device_type)
        val signal: TextView = itemView.findViewById(R.id.device_signal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_list_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int = devices.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.name.text = device.name
        holder.type.text = device.type
        holder.signal.text = "${device.signalStrength} dBm"

        val iconRes = when (device.type) {
            "WiFi" -> R.drawable.ic_wifi
            "Bluetooth" -> R.drawable.ic_bluetooth
            "Cell Tower" -> R.drawable.ic_tower
            else -> R.drawable.ic_device
        }
        holder.icon.setImageResource(iconRes)

    }
}
