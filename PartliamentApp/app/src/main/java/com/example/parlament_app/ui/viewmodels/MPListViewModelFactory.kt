package com.example.parlament_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Factory class for creating instances of MPListViewModel.
class MPListViewModelFactory(
    private val application: Application,
    private val party: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MPListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MPListViewModel(application, party) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}