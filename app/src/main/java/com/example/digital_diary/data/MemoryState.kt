package com.example.digital_diary.data

import com.example.digital_diary.R

data class MemoryState(
    val memories: List<Memory> = emptyList(),
    val photoPath: ByteArray = byteArrayOf(),
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
    )
)
