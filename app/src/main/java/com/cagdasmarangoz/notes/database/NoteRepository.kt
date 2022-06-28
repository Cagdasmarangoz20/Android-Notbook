package com.cagdasmarangoz.notes.database

import androidx.lifecycle.LiveData
import com.cagdasmarangoz.notes.database.Note
import com.cagdasmarangoz.notes.database.NotesDao

class NoteRepository(private val notesDao: NotesDao) {
    val allNotes:  LiveData<List<Note>> = notesDao.getAllNote()
    suspend fun  insert(note: Note){
        notesDao.insert(note)
    }
    suspend fun delete(note: Note)
    {
        notesDao.delete(note)
    }
    suspend fun update(note: Note){
        notesDao.update(note)
    }

}