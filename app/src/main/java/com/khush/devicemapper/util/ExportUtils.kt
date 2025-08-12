package com.khush.devicemapper.util

import android.content.Context
import android.os.Environment
import com.khush.devicemapper.ScannedDevice
import java.io.File
import java.io.FileWriter
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import java.lang.Exception


object ExportUtils {
    fun exportToCSV(context: Context, devices: List<ScannedDevice>): Boolean {
        return try {
            val file = File(context.getExternalFilesDir(null), "scan_results.csv")
            val writer = FileWriter(file)
            writer.append("Type,Name,MAC,Signal,Timestamp\n")
            devices.forEach {
                writer.append("${it.type},${it.name},${it.macAddress},${it.signalStrength},${it.timestamp}\n")
            }
            writer.flush()
            writer.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun exportToPDF(context: Context, devices: List<ScannedDevice>): Boolean {
        return try {
            val document = PdfDocument()
            val paint = Paint()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            paint.textSize = 14f
            var y = 30
            canvas.drawText("Scan Results Report", 20f, y.toFloat(), paint)
            y += 30

            for (device in devices) {
                val text = "${device.type} - ${device.name} - ${device.macAddress ?: "N/A"} - ${device.signalStrength}dBm"
                canvas.drawText(text, 20f, y.toFloat(), paint)
                y += 20
                if (y > 800) break
            }

            document.finishPage(page)

            val file = File(context.getExternalFilesDir(null), "scan_results.pdf")
            document.writeTo(FileOutputStream(file))
            document.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

