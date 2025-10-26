package com.example.gracehopperapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gracehopperapp.Filters
import com.example.gracehopperapp.TalkViewModel
import com.example.gracehopperapp.model.Talk
import com.example.gracehopperapp.ui.theme.GraceHopperAppTheme
import com.example.gracehopperapp.utils.PreviewTalkViewModel
import com.example.gracehopperapp.utils.mockTalk
import com.example.gracehopperapp.utils.mockTalks

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(talkViewModel: TalkViewModel, navController: NavHostController, bottomBar: @Composable () -> Unit ) {
    val talks by talkViewModel.filteredTalks.collectAsState()
    val filters by talkViewModel.filters.collectAsState()

    val talkTypes by talkViewModel.talkTypes.collectAsState()
    val categories by talkViewModel.categories.collectAsState()
    val experienceLevels by talkViewModel.experienceLevels.collectAsState()

    val groupedTalks = remember(talks) {
        talks.groupBy { it.fullDate }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Grace Hopper Talks") })
        },
        bottomBar = bottomBar
    ) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                FilterControls(
                    filters = filters,
                    talkTypes = talkTypes,
                    categories = categories,
                    experienceLevels = experienceLevels,
                    onFilterChanged = { type, category, experienceLevel ->
                        talkViewModel.updateFilter(type, category, experienceLevel)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (talks.isEmpty()) {
                item {
                    Text(
                        text = "No talks match the current filters.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                groupedTalks.forEach { (day, talksOnDay) ->
                    stickyHeader {
                        DayHeader(day = day)
                    }
                    items(talksOnDay, key = { it.sessionName }) { talk ->
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

@Composable
fun DayHeader(day: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun FilterControls(
    filters: Filters,
    talkTypes: List<String>,
    categories: List<String>,
    experienceLevels: List<String>,
    onFilterChanged: (type: String?, category: String?, experienceLevel: String?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterDropdown(
                label = "Type",
                options = listOf("All") + talkTypes,
                selectedValue = filters.type,
                modifier = Modifier.weight(1f),
                onSelected = { newType -> onFilterChanged(newType, null, null) }
            )

            FilterDropdown(
                label = "Category",
                options = listOf("All") + categories,
                selectedValue = filters.category,
                modifier = Modifier.weight(1f),
                onSelected = { newCategory -> onFilterChanged(null, newCategory, null) }
            )
        }

        FilterDropdown(
            label = "Experience Level",
            options = listOf("All") + experienceLevels,
            selectedValue = filters.experienceLevel,
            modifier = Modifier.fillMaxWidth(),
            onSelected = { newExperienceLevel -> onFilterChanged(null, null, newExperienceLevel) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selectedValue: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TalkItem(talk: Talk, onClicked: () -> Unit, onFavoriteClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClicked
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = talk.sessionName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onFavoriteClicked) {
                    Icon(
                        imageVector = if (talk.isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (talk.isFavorited) "Remove from favorites" else "Add to favorites",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "By ${talk.speakerNames}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (talk.timeOfDay.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = talk.timeOfDay,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (talk.category.isNotBlank()) {
                    AssistChip(onClick = { }, label = { Text(talk.category) })
                }
                if (talk.type.isNotBlank()) {
                    AssistChip(onClick = { }, label = { Text(talk.type) })
                }
                if (talk.capitalizedExperienceLevel.isNotBlank()) {
                    AssistChip(onClick = { }, label = { Text(talk.capitalizedExperienceLevel) })
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val navController = rememberNavController()

    GraceHopperAppTheme {
        HomeScreen(PreviewTalkViewModel(mockTalk, mockTalks), navController, bottomBar = {})
    }
}
