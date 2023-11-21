package com.example.tugasppapb12.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasppapb12.database.Note
import com.example.tugasppapb12.database.NoteDao
import com.example.tugasppapb12.database.NoteRoomDatabase
import com.example.tugasppapb12.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.noteDao()!!

        with (binding) {
            btnAdd.setOnClickListener {
                startActivity(Intent(
                    this@MainActivity, DetailActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes() {
        mNoteDao.allNotes.observe(this) { notes ->
            val adapterNote = NoteAdapter(notes) { note ->
                startActivity(
                    Intent(
                        this@MainActivity, DetailActivity::class.java)
                        .putExtra("note", note)
                )
            }

            adapterNote.setOnClickDeleteListener { note ->
                deleteNote(note)
            }

            with(binding) {
                recyclerView.apply {
                    adapter = adapterNote
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    private fun deleteNote(note: Note) {
        executorService.execute {
            mNoteDao.delete(note)
        }
    }
}
