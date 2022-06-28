package com.cagdasmarangoz.notes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagdasmarangoz.notes.*
import com.cagdasmarangoz.notes.database.Note
import com.cagdasmarangoz.notes.model.NoteViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {
    lateinit var notesRv : RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesRv= findViewById(R.id.idRVNotes)
        addFAB= findViewById(R.id.idFABAddNote)
        notesRv.layoutManager=LinearLayoutManager(this)

        val noteRVAdapter= NoteRVAdapter(this,this,this)
        notesRv.adapter = noteRVAdapter
        viewModel= ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NoteViewModal::class.java)
    viewModel.allNotes.observe(this) { list ->
        list?.let {
            noteRVAdapter.updateList(it)
        }
    }
        addFAB.setOnClickListener{
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNoteClick(note: Note) {
      val intent =Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType","Edit")
        intent.putExtra("noteTitle",note.noteTitle)
        intent.putExtra("noteDescription",note.noteDescription)
        intent.putExtra("noteID",note.id)
        startActivity(intent)
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_SHORT).show()
    }

}