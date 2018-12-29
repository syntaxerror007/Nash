package com.android.nash.backgroundservice

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import com.android.nash.R
import com.android.nash.data.CustomerDataModel
import com.android.nash.provider.CustomerProvider
import com.android.nash.util.convertToString
import com.android.nash.util.toNashDate
import de.siegmar.fastcsv.writer.CsvAppender
import de.siegmar.fastcsv.writer.CsvWriter
import io.reactivex.Observable
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*


class CustomerDownloadService : IntentService(CustomerDownloadService::class.simpleName) {
    private val mCustomerProvider = CustomerProvider()
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val notificationId = 0xFF
    private val notificationChannelId = "com.android.nash.notification.channel"
    private val notificationChannelName = "Download Data"
    private val notificationChannelDescription = "Notification for downloading customer data"
    private val notificationImportance = NotificationManager.IMPORTANCE_DEFAULT


    override fun onHandleIntent(intent: Intent?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationCompat.Builder(applicationContext, CustomerDownloadService::class.simpleName!!)
        initNotificationChannel()
        notificationBuilder.apply {
            setContentTitle("Downloading Customer Data")
            setOngoing(true)
            setSmallIcon(R.drawable.ic_export_excel)
            setChannelId(notificationChannelId)
        }

        val appender = CsvWriter().append(getFile(), StandardCharsets.UTF_8)
        writeLine(CustomerDataModel.getCsvHeader(), appender)
        var downloadedCustomer = 0
        var totalCustomer = 0
        val temp = mCustomerProvider.getAllCustomerKey()
                .concatMapIterable {
                    totalCustomer = it.size
                    it
                }
                .concatMapMaybe {
                    mCustomerProvider.getCustomerFromUUID(it)
                }.concatMap {
                    try {
                        downloadedCustomer++
                        notificationBuilder.apply {
                            setContentText("Downloading $downloadedCustomer/$totalCustomer")
                            setProgress(totalCustomer, downloadedCustomer, false)
                        }
                        notificationManager.notify(notificationId, notificationBuilder.build())
                        val rowData = it.toCsvRow()
                        writeLine(rowData, appender)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return@concatMap Observable.just(it)
                }.toList().subscribe({
                    notificationBuilder.apply {
                        setContentTitle("Nash - Download Finish")
                        setContentText("$totalCustomer Customer Data has been downloaded")
//                        val path = Uri.fromFile(file)
//                        val intent = Intent(Intent.ACTION_VIEW).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            setDataAndType(path, "application/csv")
//                        }
//                        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//                        addAction(R.drawable.ic_export_excel, "Open File", pendingIntent)
                        setProgress(0, 0, false)
                        setOngoing(false)
                    }
                    notificationManager.notify(notificationId, notificationBuilder.build())
                }) {
                    notificationBuilder.apply {
                        setOngoing(false)
                    }
                    notificationManager.notify(notificationId, notificationBuilder.build())
                    it.printStackTrace()
                }
    }

    private fun getFile(): File {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Nash Customer (${Calendar.getInstance().toNashDate().convertToString()}).csv")
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    private fun initNotificationChannel() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(NotificationChannel(notificationChannelId, notificationChannelName, notificationImportance))
    } else {

    }

    private fun writeLine(temps: Array<String>, appender: CsvAppender) {
        appender.apply {
            temps.forEach { appendField(it) }
            endLine()
            flush()
        }
    }
}