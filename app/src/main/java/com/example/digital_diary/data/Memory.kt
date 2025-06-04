package com.example.digital_diary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val photoPath: ByteArray,
    val audioPath: String?,
    val description: String?,
    val mood: Int?,
)
