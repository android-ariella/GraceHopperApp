package com.example.gracehopperapp.utils

import android.app.Application
import com.example.gracehopperapp.TalkViewModel
import com.example.gracehopperapp.model.Talk
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewTalkViewModel(initialTalk: Talk? = null, previewTalks: List<Talk> = emptyList()) : TalkViewModel(
    Application()
) {
    override fun loadTalksFromJson() {
    }

    init {
        if (previewTalks.isNotEmpty()) {
            val talksField = TalkViewModel::class.java.getDeclaredField("_talks")
            talksField.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val talksFlow = talksField.get(this) as MutableStateFlow<List<Talk>>
            talksFlow.value = previewTalks
        }
        if (initialTalk != null) {
            onTalkSelected(initialTalk)
        }
    }
}

val mockTalk = Talk(
    sessionName = "Getting Started with GraphQL: Build Smarter APIs",
    speakerNames = "Gosia Waszak, Stuart Perks",
    time = "Thursday, November 06, 04:00 PM - 05:00 PM",
    description = "GraphQL is revolutionizing the way developers build and consume APIs. In this fast-paced, beginner-friendly workshop, attendees will build and query their very first GraphQL API using JavaScript and Apollo Server.",
    type = "Workshop",
    category = "Technology",
    experienceLevel = "Beginner",
    isFavorited = false
)

val mockTalks = listOf(
    Talk(
        sessionName = "Getting Started with GraphQL: Build Smarter APIs",
        description = "GraphQL is revolutionizing how developers build and consume APIs. In this fast-paced, beginner-friendly workshop, you'll build and query your first GraphQL API using JavaScript and Apollo Server.",
        speakerNames = "Gosia Waszak, Stuart Perks",
        time = "Thursday, November 06, 04:00 PM - 05:00 PM",
        type = "Workshop",
        category = "Technology",
        experienceLevel = "beginner",
        isFavorited = false
    ),
    Talk(
        sessionName = "Mastering Jetpack Compose Layouts",
        description = "Learn advanced layout techniques in Jetpack Compose including ConstraintLayout, IntrinsicSize, and performance optimization for complex UIs.",
        speakerNames = "Ada Lovelace",
        time = "Friday, November 07, 02:00 PM - 03:00 PM",
        type = "Session",
        category = "Android",
        experienceLevel = "intermediate",
        isFavorited = true
    ),
    Talk(
        sessionName = "Building Resilient Offline Apps",
        description = "Explore strategies for offline-first app design, data synchronization, and conflict resolution patterns in Android apps.",
        speakerNames = "Grace Hopper",
        time = "Friday, November 07, 11:00 AM - 12:00 PM",
        type = "Talk",
        category = "Mobile",
        experienceLevel = "advanced",
        isFavorited = false
    ),
    Talk(
        sessionName = "Coroutines Deep Dive: Patterns and Pitfalls",
        description = "Understand structured concurrency, exception handling, and context management for production-grade coroutine use in Kotlin.",
        speakerNames = "Linus Torvalds",
        time = "Saturday, November 08, 01:00 PM - 02:00 PM",
        type = "Talk",
        category = "Kotlin",
        experienceLevel = "intermediate",
        isFavorited = false
    ),
    Talk(
        sessionName = "Testing Compose UI Like a Pro",
        description = "Learn to write snapshot tests, use semantics for accessibility, and automate Compose UI verification for scalable QA.",
        speakerNames = "Margaret Hamilton",
        time = "Saturday, November 08, 10:00 AM - 11:00 AM",
        type = "Workshop",
        category = "Testing",
        experienceLevel = "beginner",
        isFavorited = false
    )
)