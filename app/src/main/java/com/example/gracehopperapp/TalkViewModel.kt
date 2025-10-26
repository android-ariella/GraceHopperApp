package com.example.gracehopperapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gracehopperapp.model.Talk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class Filters(
    val type: String = "All",
    val category: String = "All",
    val experienceLevel: String = "All"
)

open class TalkViewModel(application: Application) : AndroidViewModel(application) {
    private val json = Json { ignoreUnknownKeys = true }

    private val _filters = MutableStateFlow(Filters())
    val filters: StateFlow<Filters> = _filters

    private val _talks = MutableStateFlow<List<Talk>>(emptyList())

    private val _selectedTalk = MutableStateFlow<Talk?>(null)
    open val selectedTalk: StateFlow<Talk?> = _selectedTalk

    fun onTalkSelected(talk: Talk) {
        _selectedTalk.value = talk
    }

    fun clearSelectedTalk() {
        _selectedTalk.value = null
    }

    val favoritedTalks: StateFlow<List<Talk>> = _talks.map { allTalks ->
        allTalks.filter { it.isFavorited }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun toggleFavorite(talk: Talk) {
        val currentTalks = _talks.value.toMutableList()
        val index = currentTalks.indexOfFirst { it.sessionName == talk.sessionName }
        if (index != -1) {
            val oldTalk = currentTalks[index]
            val newTalk = oldTalk.copy(isFavorited = !oldTalk.isFavorited)
            currentTalks[index] = newTalk
            _talks.value = currentTalks
            if (_selectedTalk.value?.sessionName == newTalk.sessionName) {
                _selectedTalk.value = newTalk
            }
        }
    }

    val filteredTalks: StateFlow<List<Talk>> =
        combine(_talks, _filters) { talks, filters ->
            talks.filter { talk ->
                (filters.type == "All" || talk.type == filters.type) &&
                    (filters.category == "All" || talk.category == filters.category) &&
                       (filters.experienceLevel == "All"
                               || talk.capitalizedExperienceLevel == filters.experienceLevel)
            }
        }.let { flow ->
            val stateFlow = MutableStateFlow(emptyList<Talk>())
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                flow.collect { stateFlow.value = it }
            }
            stateFlow.asStateFlow()
        }

    init {
        loadTalksFromJson()
    }

    fun updateFilter(type: String? = null, category: String? = null, experienceLevel: String? = null) {
        _filters.value = _filters.value.copy(
            type = type ?: _filters.value.type,
            category = category ?: _filters.value.category,
            experienceLevel = experienceLevel ?: _filters.value.experienceLevel
        )
    }

    open fun loadTalksFromJson() {
        val context = getApplication<Application>().applicationContext
        val talkList = runCatching {
            context.assets.open("TalkData.json").bufferedReader().use { reader ->
                json.decodeFromString<List<Talk>>(reader.readText())
            }
        }.getOrElse {
            emptyList()
        }
        _talks.value = talkList.distinctBy { it.sessionName }
    }

    val talkTypes: StateFlow<List<String>> = _talks.map { talks ->
        talks.map { it.type }.distinct().filter {it.isNotBlank()}.sorted()
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = _talks.map { talks ->
        talks.map { it.category }.distinct().filter {it.isNotBlank()}.sorted()
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())

    val experienceLevels: StateFlow<List<String>> = _talks.map { talks ->
        talks.map { it.capitalizedExperienceLevel }.distinct().filter {it.isNotBlank()}.sorted()
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())
}