package com.example.digital_diary.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {

    @Upsert
    suspend fun insertMemory(memory: Memory)

    @Delete
    suspend fun deleteMemory(memory: Memory)

    @Query("SELECT * FROM Memory WHERE description LIKE '%' || :word || '%'")
    fun getMemoriesWithMatchingWord(word: String): Flow<List<Memory>>
}