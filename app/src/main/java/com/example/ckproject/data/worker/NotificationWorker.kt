package com.example.ckproject.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.ckproject.ui.shareHistory.ShareHistoryActivity
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationWorker(val appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {

    companion object {
        private const val REMINDER_WORK_NAME = "notification"
        private const val PARAM_NAME = "triggeredTime"


        fun runAt(hourOfTheDay: Int) {
            val workManager = WorkManager.getInstance()

            val calendar = Calendar.getInstance()



            calendar.set(Calendar.HOUR_OF_DAY, hourOfTheDay)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val currentTime = System.currentTimeMillis()
            val specificTimeToTrigger: Long = calendar.timeInMillis

            val delayToPass = if (currentTime > specificTimeToTrigger) {

                calendar.set(Calendar.HOUR_OF_DAY, 16)
                if (currentTime < calendar.timeInMillis) {
                    calendar.timeInMillis - currentTime
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 10)
                    calendar.timeInMillis - currentTime
                }
            } else {
                specificTimeToTrigger - currentTime
            }

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val m = calendar.get(Calendar.MINUTE)
            val data = workDataOf(PARAM_NAME to hour)

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()

            workManager.enqueueUniqueWork(
                    REMINDER_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    workRequest
            )
        }

    }


    override fun doWork(): Result {

        val triggeredTime = inputData.getInt(PARAM_NAME, 0)
        val mi = inputData.getInt("minutes", 0)

        val channelId = "My_Channel_ID"
        val notificationId = 1

        createNotificationChannel(channelId)
        displayNotification(channelId, notificationId)

        val nextSchdulingTime = if (triggeredTime == 10)
            16
        else
            10
        runAt(nextSchdulingTime)

        return Result.success()

    }

    private fun createNotificationChannel(channelId: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyChannel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance)

            val notificationManager =
                    appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun displayNotification(channelId: String, notificationId: Int) {


        val intent = Intent(appContext, ShareHistoryActivity::class.java)
        val pendIntent = PendingIntent.getActivity(appContext, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
                .setContentIntent(pendIntent)
                .setSmallIcon(com.example.ckproject.R.drawable.ic_cosmetics)
                .setContentTitle("Safer cosmetics for women")
                .setContentText("affordable brand has been around since 1985.")
                .setStyle(
                        NotificationCompat.BigPictureStyle()
                                .bigPicture(
                                        BitmapFactory.decodeResource(
                                                appContext.resources,
                                                com.example.ckproject.R.drawable.cosmetics
                                        )
                                )
                                .bigLargeIcon(
                                        BitmapFactory.decodeResource(
                                                appContext.resources,
                                                com.example.ckproject.R.drawable.cosmetics
                                        )
                                )
                                .setBigContentTitle("LA Girl Cosmetics Pro concealer")
                                .setSummaryText("The signature product from LA Girl Cosmetics ")
                )


        with(NotificationManagerCompat.from(appContext)) {
            notify(notificationId, notificationBuilder.build())
        }
    }
}