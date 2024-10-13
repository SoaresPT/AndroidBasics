package com.example.parlament_app.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.parlament_app.ui.viewmodels.MPListViewModel
import com.example.parlament_app.ui.viewmodels.MPListViewModelFactory

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Composable screen displaying a list of MPs belonging to a selected party.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MPListScreen(navController: NavHostController, party: String) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: MPListViewModel = viewModel(
        factory = MPListViewModelFactory(application, party)
    )
    val mps by viewModel.mps.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("$party MPs") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(mps.size) { index ->
                val mp = mps[index]
                Text(
                    text = "${mp. firstname} ${mp.lastname}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("mp_detail/${mp.hetekaId}")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}
