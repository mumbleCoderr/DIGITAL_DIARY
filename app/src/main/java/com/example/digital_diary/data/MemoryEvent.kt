package com.example.digital_diary.data

import java.time.LocalDate
import java.util.Date

sealed interface MemoryEvent {
    object SaveMemory: MemoryEvent
    data class SetPhotoPath(val photoPath: String?): MemoryEvent
    data class SetAudioPath(val audioPath: String?): MemoryEvent
    data class SetDescription(val description: String): MemoryEvent
    data class SetMood(val mood: Int?): MemoryEvent
    object ShowDialog: MemoryEvent
    object HideDialog: MemoryEvent
    data class DeleteMemory(val memory: Memory): MemoryEvent
    data class SetCity(val city: String?): MemoryEvent
    data class SetDate(val date: String?): MemoryEvent
    object ClearMemory: MemoryEvent
}