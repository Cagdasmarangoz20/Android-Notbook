package com.cagdasmarangoz.notes.database

class NoteRepository(private val notesDao: NotesDao) {

    suspend fun getAll() = notesDao.getAllNote()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun searchDatabase(searchQuery: String): List<Note> {
        return notesDao.searchDatabase(searchQuery)
    }

}