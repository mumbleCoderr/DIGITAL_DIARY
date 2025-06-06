package com.example.digital_diary.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity
data class Memory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val photoPath: String?,
    val audioPath: String?,
    val description: String?,
    val mood: Int?,
    val city: String?,
    val date: String?,
)
