package com.example.gracehopperapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Talk(
    @SerialName("session_name") val sessionName: String,
    val description: String,
    @SerialName("speaker_name(s)") val speakerNames: String,
    val time: String,
    val type: String,
    val category: String,
    @SerialName("experience_level") val experienceLevel: String,
    val isFavorited: Boolean = false
) {
    val capitalizedExperienceLevel: String
        get() = experienceLevel.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    val fullDate: String
        get() {
            val parts = time.split(',')
            return if (parts.size >= 2) {
                "${parts[0].trim()}, ${parts[1].trim()}"
            } else {
                time.substringBefore(",").trim()
            }
        }

    val timeOfDay: String
        get() {
            val lastCommaIndex = time.lastIndexOf(',')
            return if (lastCommaIndex != -1) {
                time.substring(lastCommaIndex + 1).trim()
            } else {
                ""
            }
        }
}