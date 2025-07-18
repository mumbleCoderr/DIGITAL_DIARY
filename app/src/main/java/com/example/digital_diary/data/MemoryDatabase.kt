package com.example.digital_diary.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Memory::class],
    version = 12,
)

abstract class MemoryDatabase: RoomDatabase() {
    abstract val dao: MemoryDao
}