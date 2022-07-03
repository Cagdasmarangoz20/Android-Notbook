package com.cagdasmarangoz.notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.concurrent.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Delete
    suspend fun delete(note: Note)

    @Query("Select * From notesTable order by id ASC")
    suspend fun getAllNote():List<Note>

    @Query("SELECT * FROM notesTable WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery ||  '%'")
    suspend fun searchDatabase(searchQuery:String): List<Note>
}