package com.example.digital_diary.data

import com.example.digital_diary.R
import java.time.LocalDate
import java.util.Date

data class MemoryState(
    val memories: List<Memory> = emptyList(),
    val photoPath: String? = null,
    val audioPath: String? = null,
    val description: String? = null,
    val mood: Int? = null,
    val isAddingMemory: Boolean = false,
    val moodList: List<Int> = listOf(
        R.drawable.love,
        R.drawable.cry,
        R.drawable.mid,
        R.drawable.sad,
        R.drawable.angry,
        R.drawable.superhappy,
    ),
    val city: String? = null,
    val date: String? = null,
    val memoryId: Int? = null
)
