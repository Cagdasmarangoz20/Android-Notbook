package com.cagdasmarangoz.notes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Delete
    suspend fun delete(note: Note)

    @Query("Select * From notesTable order by id ASC")
    fun getAllNote():LiveData<List<Note>>
}