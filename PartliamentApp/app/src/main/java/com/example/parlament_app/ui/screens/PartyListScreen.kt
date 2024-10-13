package com.example.parlament_app.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.parlament_app.ui.viewmodels.PartyListViewModel
import com.example.parlament_app.ui.viewmodels.PartyListViewModelFactory

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Composable screen displaying a list of political parties.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyListScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: PartyListViewModel = viewModel(
        factory = PartyListViewModelFactory(application)
    )
    val parties by viewModel.parties.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Parties") })
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(parties.size) { index ->
                val party = parties[index]
                Text(
                    text = party,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("mp_list/$party")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}
