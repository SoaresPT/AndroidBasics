package com.example.parlament_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Factory class for creating instances of MPDetailViewModel.
class MPDetailViewModelFactory(
    private val application: Application,
    private val mpId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MPDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MPDetailViewModel(application, mpId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}