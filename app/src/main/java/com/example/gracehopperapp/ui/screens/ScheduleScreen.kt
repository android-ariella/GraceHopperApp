package com.example.gracehopperapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gracehopperapp.TalkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(talkViewModel: TalkViewModel, navController: NavController, bottomBar: @Composable () -> Unit) {
    val favoritedTalks by talkViewModel.favoritedTalks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Schedule") })
        },
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (favoritedTalks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No talks added to your schedule yet.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Tap the heart on a talk to add it to your schedule.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(favoritedTalks, key = { it.sessionName }) { talk ->
                        TalkItem(
                            talk = talk,
                            onClicked = {
                                talkViewModel.onTalkSelected(talk)
                                navController.navigate("details")
                            },
                            onFavoriteClicked = {
                                talkViewModel.toggleFavorite(talk)
                            }
                        )
                    }
                }
            }
        }
    }
}