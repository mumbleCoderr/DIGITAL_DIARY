package com.example.digital_diary.data

data class MemoryState(
    val memories: List<Memory> = emptyList(),
    val photoPath: String? = null,
    val audioPath: String? = null,
    val description: String? = null,
    val mood: Int? = null,
    val isAddingMemory: Boolean = false,
)
