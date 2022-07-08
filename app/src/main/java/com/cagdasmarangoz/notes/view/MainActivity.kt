package com.cagdasmarangoz.notes.view

import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.cagdasmarangoz.notes.*
import com.cagdasmarangoz.notes.core.ViewBindingActivity
import com.cagdasmarangoz.notes.database.DarkModeData
import com.cagdasmarangoz.notes.database.Note
import com.cagdasmarangoz.notes.database.NoteDatabase
import com.cagdasmarangoz.notes.databinding.ActivityMainBinding
import com.cagdasmarangoz.notes.model.NoteViewModal
import java.util.*
import android.view.MenuItem as MenuItem1


class MainActivity : ViewBindingActivity<ActivityMainBinding>(),
    NoteClickInterface,
    NoteClickDeleteInterface,
    SearchView.OnQueryTextListener {

    lateinit var viewModel: NoteViewModal
    private lateinit var noteArrayList: ArrayList<NoteDatabase>
    private lateinit var darkModeData: DarkModeData
    val noteRVAdapter = NoteRVAdapter(this, this, this)
    private var isSearching = false
    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)


        noteArrayList = arrayListOf<NoteDatabase>()

        navigationMenu()



    binding.idRVNotes.apply {

        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        adapter = noteRVAdapter }



        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModal::class.java]



        viewModel.allNotes.observe(this) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
                isSearching = false
            }
        }
        binding.idFABAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, getString(R.string.note_deleted, note.noteTitle), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        val item = menu.findItem(R.id.idSearch)
        val searchView = item?.actionView as? SearchView

        searchView?.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem1): Boolean {
        when (item.itemId) {
            R.id.idDarkTheme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.idLightTheme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
          R.id.idList -> {

              binding.idRVNotes.apply {

                  layoutManager = LinearLayoutManager(this@MainActivity)
                  adapter = noteRVAdapter
              }
          }
            R.id.idGrid -> {

                binding.idRVNotes.apply {

                    layoutManager = GridLayoutManager(this@MainActivity,2)
                    adapter = noteRVAdapter
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun noteSetting() {

        darkModeData = DarkModeData(this@MainActivity)


        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        val switch = preferenceManager.getBoolean("darkMode", false)

        if (switch) {
            darkModeData.SetDarkModeState(false)
        } else {
            darkModeData.SetDarkModeState(true)
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


    fun navigationMenu() {

        val navView= binding.idNavView
        val drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.idHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.id_about ->{
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }


}

