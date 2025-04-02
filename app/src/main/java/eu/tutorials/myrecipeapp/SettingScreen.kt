package eu.tutorials.myrecipeapp.ui.screens


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import eu.tutorials.myrecipeapp.worker.NewsUpdateWorker

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("news_preferences", Context.MODE_PRIVATE) }
    val notificationsEnabled = remember {
        mutableStateOf(prefs.getBoolean("notifications_enabled", true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "News Notifications",
                style = MaterialTheme.typography.bodyLarge
            )

            Switch(
                checked = notificationsEnabled.value,
                onCheckedChange = { enabled ->
                    notificationsEnabled.value = enabled
                    prefs.edit().putBoolean("notifications_enabled", enabled).apply()

                    if (enabled) {
                        // Schedule notifications
                        NewsUpdateWorker.scheduleNewsUpdates(context)
                    } else {
                        // Cancel notifications
                        WorkManager.getInstance(context)
                            .cancelUniqueWork(NewsUpdateWorker.WORK_NAME)
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Add more settings options as needed
    }
}