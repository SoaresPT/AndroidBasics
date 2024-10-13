package com.example.parlament_app.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.parlament_app.data.ParliamentDatabase
import com.example.parlament_app.data.repository.MPRepository

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: WorkManager worker class that refreshes MP data periodically.
class MPRefreshWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository: MPRepository

    init {
        val database = ParliamentDatabase.getDatabase(context)
        repository = MPRepository(database.mpDao())
    }

    override suspend fun doWork(): Result {
        return try {
            repository.refreshMPs()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}