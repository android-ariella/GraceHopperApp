package com.example.gracehopperapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        selectedTalk?.let { talkDetails ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = talkDetails.sessionName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                DetailItem(label = "Speaker(s)", value = talkDetails.speakerNames)
                DetailItem(label = "Time", value = talkDetails.time)
                DetailItem(label = "Description", value = talkDetails.description)

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (talkDetails.type.isNotBlank()) AssistChip(
                        onClick = {},
                        label = { Text(talkDetails.type) })
                    if (talkDetails.category.isNotBlank()) AssistChip(
                        onClick = {},
                        label = { Text(talkDetails.category) })
                    if (talkDetails.capitalizedExperienceLevel.isNotBlank()) AssistChip(
                        onClick = {},
                        label = { Text(talkDetails.capitalizedExperienceLevel) })
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
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