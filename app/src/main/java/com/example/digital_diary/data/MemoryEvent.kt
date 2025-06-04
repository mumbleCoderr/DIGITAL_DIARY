package com.example.digital_diary.data

sealed interface MemoryEvent {
    object SaveMemory: MemoryEvent
    data class SetPhotoPath(val photoPath: ByteArray): MemoryEvent
    data class SetAudioPath(val audioPath: String?): MemoryEvent
    data class SetDescription(val description: String): MemoryEvent
    data class SetMood(val mood: Int?): MemoryEvent
    object ShowDialog: MemoryEvent
    object HideDialog: MemoryEvent
    data class DeleteMemory(val memory: Memory): MemoryEvent
}