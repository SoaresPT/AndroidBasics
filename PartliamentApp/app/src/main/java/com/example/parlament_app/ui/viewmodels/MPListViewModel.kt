package com.example.parlament_app.ui.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parlament_app.data.ParliamentDatabase
import com.example.parlament_app.data.model.MP
import com.example.parlament_app.data.repository.MPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: ViewModel for the MPListScreen.
class MPListViewModel(application: Application, party: String) : AndroidViewModel(application) {
    private val _mps = MutableStateFlow<List<MP>>(emptyList())
    val mps: StateFlow<List<MP>> = _mps

    private val repository: MPRepository

    init {
        val database = ParliamentDatabase.getDatabase(application)
        repository = MPRepository(database.mpDao())
        viewModelScope.launch {
            repository.getMPsByParty(party).collect { mpList ->
                _mps.value = mpList.sortedBy { it.lastname }
            }
        }
    }
}