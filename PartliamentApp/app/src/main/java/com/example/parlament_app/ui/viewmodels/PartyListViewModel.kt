package com.example.parlament_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parlament_app.data.ParliamentDatabase
import com.example.parlament_app.data.repository.MPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: ViewModel for the PartyListScreen.
class PartyListViewModel(application: Application) : AndroidViewModel(application) {
    private val _parties = MutableStateFlow<List<String>>(emptyList())
    val parties: StateFlow<List<String>> = _parties

    private val repository: MPRepository

    init {
        val database = ParliamentDatabase.getDatabase(application)
        repository = MPRepository(database.mpDao())
        viewModelScope.launch {
            repository.allMPs.collect { mps ->
                val partySet = mps.mapNotNull { it.party }.toSet()
                _parties.value = partySet.toList().sorted()
            }
        }
    }
}