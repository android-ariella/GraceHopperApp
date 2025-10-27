package com.example.gracehopperapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gracehopperapp.TalkViewModel
import com.example.gracehopperapp.ui.theme.GraceHopperAppTheme
import com.example.gracehopperapp.utils.PreviewTalkViewModel
import com.example.gracehopperapp.utils.mockTalk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(viewModel: TalkViewModel, navController: NavHostController) {
    val selectedTalk by viewModel.selectedTalk.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Talk Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearSelectedTalk()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    selectedTalk?.let {
                        IconButton(onClick = { viewModel.toggleFavorite(it) }) {
                            Icon(
                                imageVector = if (it.isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (it.isFavorited) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // The selectedTalk is already collected and available for you to use.
        selectedTalk?.let { talkDetails ->
            // TODO: Build the details UI here using the 'talkDetails' object.
            // You can start by creating a Column and adding Text Composables
            // for the session name, speaker, time, and description.
            //TODO: add AssistChip Composable
            Column(modifier = Modifier.padding(paddingValues)) {
                Text(text = "Details screen is ready to be built!")
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
// TODO: Build UI here for displaying speakers, time, and description.
// You can start with a Column that contains the label text as a title, then put the value in a body style.
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {

    val navController = rememberNavController()

    GraceHopperAppTheme {
        DetailsScreen(viewModel = PreviewTalkViewModel(mockTalk), navController = navController)
    }
}

