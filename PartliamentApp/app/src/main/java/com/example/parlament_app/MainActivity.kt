package com.example.parlament_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.*
import com.example.parlament_app.ui.Navigation
import com.example.parlament_app.ui.theme.Parliament_Feedback_AppTheme
import com.example.parlament_app.workmanager.MPRefreshWorker
import java.util.concurrent.TimeUnit

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Main activity that sets up the app's UI and schedules data refresh work.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDataRefresh()


        setContent {
            Parliament_Feedback_AppTheme {
                Navigation()
            }
        }
    }

    // Schedules periodic data refresh using WorkManager
    private fun scheduleDataRefresh() {
        val workRequest = PeriodicWorkRequestBuilder<MPRefreshWorker>(12, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "MPRefreshWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}