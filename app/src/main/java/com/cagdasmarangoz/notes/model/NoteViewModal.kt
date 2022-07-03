package com.cagdasmarangoz.notes.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cagdasmarangoz.notes.database.Note
import com.cagdasmarangoz.notes.database.NoteDatabase
import com.cagdasmarangoz.notes.database.NoteRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModal(application: Application) : AndroidViewModel(application) {

    private val _allNotes = MutableLiveData<List<Note>>()
    val allNotes: LiveData<List<Note>> get() = _allNotes


    val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        viewModelScope.launch{
                _allNotes.value = repository.getAll()
        }

    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun searchDatabase(searchQuery: String) {
        viewModelScope.launch {
            _allNotes.value = repository.searchDatabase(searchQuery)
        }
    }
}