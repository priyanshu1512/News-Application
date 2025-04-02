package eu.tutorials.myrecipeapp.worker



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import eu.tutorials.mynewsapp.newsService
import eu.tutorials.myrecipeapp.MainActivity
import eu.tutorials.myrecipeapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class NewsUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "news_update_worker"
        const val CHANNEL_ID = "news_notification_channel"
        private const val NOTIFICATION_ID = 1

        // Function to schedule periodic work
        fun scheduleNewsUpdates(context: Context) {
            val constraints = androidx.work.Constraints.Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest = androidx.work.PeriodicWorkRequestBuilder<NewsUpdateWorker>(
                1, TimeUnit.HOURS,  // Check every 3 hours
                30, TimeUnit.MINUTES // Flex period
            )
                .setConstraints(constraints)
                .build()

            androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Create notification channel (required for Android O+)
            createNotificationChannel()

            // Fetch the latest news
            val response = newsService.getTopHeadlines()

            // Check if there are any news articles
            if (response.articles.isNotEmpty()) {
                val latestArticle = response.articles.first()

                // Create intent that opens the app when notification is clicked
                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                // Build the notification
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Latest News")
                    .setContentText(latestArticle.title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(latestArticle.title))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                // Show the notification
                val notificationManager = applicationContext.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

                notificationManager.notify(NOTIFICATION_ID, notification)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "News Updates"
            val descriptionText = "Notifications for latest news"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}