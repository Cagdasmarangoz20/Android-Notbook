package com.cagdasmarangoz.notes.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cagdasmarangoz.notes.R
import com.cagdasmarangoz.notes.adapter.NoteRVAdapter
import com.cagdasmarangoz.notes.core.ViewBindingFragment
import com.cagdasmarangoz.notes.databinding.FragmentHomeBinding
import com.cagdasmarangoz.notes.ui.NoteViewModal
import com.cagdasmarangoz.notes.ui.AddEditNoteActivity
import java.util.*

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(), MenuProvider,
    SearchView.OnQueryTextListener {

    private lateinit var viewModel: NoteViewModal
    private val noteRVAdapter = NoteRVAdapter(
        onNoteClick = { note ->
            val intent = Intent(context, AddEditNoteActivity::class.java)
            intent.putExtra("noteType", "Edit")
            intent.putExtra("noteTitle", note.noteTitle)
            intent.putExtra("noteDescription", note.noteDescription)
            intent.putExtra("noteID", note.id)
            startActivity(intent)

        },
        onNoteDeleteClick = { note ->
            viewModel.deleteNote(note)
            Toast.makeText(
                context,
                getString(R.string.note_deleted, note.noteTitle),
                Toast.LENGTH_SHORT
            )
                .show()
            viewModel.refresh()
        }
    )
    private var isSearching = false
    private var timer = Timer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.idRVNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteRVAdapter
        }

        activity?.let {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(it.application)
            )[NoteViewModal::class.java]
        }

        viewModel.allNotes.observe(viewLifecycleOwner) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
                isSearching = false
            }
        }
        binding.idFABAddNote.setOnClickListener {
            val intent = Intent(context, AddEditNoteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_top, menu)
        val item = menu.findItem(R.id.idSearch)
        val searchView = item?.actionView as? SearchView

        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.idDarkTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            R.id.idLightTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            R.id.idList -> {
                binding.idRVNotes.apply {

                    layoutManager = LinearLayoutManager(context)
                    adapter = noteRVAdapter
                }
                true
            }
            R.id.idGrid -> {
                binding.idRVNotes.apply {

                    layoutManager = GridLayoutManager(context, 2)
                    adapter = noteRVAdapter
                }
                true
            }
            else -> false
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val text = query ?: return false
        viewModel.searchDatabase(text)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (isSearching) return false
        val text = newText ?: return false

        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                isSearching = true
                viewModel.searchDatabase(text)
            }
        }, 1000)

        return false
    }
}