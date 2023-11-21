package com.example.tugasppapb12.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugasppapb12.database.Note
import com.example.tugasppapb12.database.NoteDao
import com.example.tugasppapb12.database.NoteRoomDatabase
import com.example.tugasppapb12.databinding.ActivityDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
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
            btnBack.setOnClickListener {
                onBackPressed()
            }
            if (intent.hasExtra("note")) {
                val note = intent.getSerializableExtra("note") as Note

                editTitle.setText(note.title)
                editDescription.setText(note.description)

                btnSave.setOnClickListener {
                    val editedNote = Note(
                        id = note.id,
                        title = editTitle.text.toString(),
                        description = editDescription.text.toString()
                    )

                    update(editedNote)
                    returnToMainActivity(editedNote)
                    Toast.makeText(this@DetailActivity, "Note updated",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else {
                btnSave.setOnClickListener {
                    if(editTitle.text.toString().isNotEmpty() &&
                        editDescription.text.toString().isNotEmpty()) {
                        val newNote = Note(
                            title = editTitle.text.toString(),
                            description = editDescription.text.toString()
                        )

                        insert(newNote)
                        returnToMainActivity(newNote)
                        Toast.makeText(this@DetailActivity, "Note saved",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        if(editTitle.text.toString().isEmpty()) {
                            editTitle.error = "Title must not be empty"
                        }
                        if(editDescription.text.toString().isEmpty()) {
                            editDescription.error = "Description must not be empty"
                        }
                    }
                }
            }
        }
    }

    private fun update(note: Note) {
        executorService.execute {
            mNoteDao.update(note)
        }
    }

    private fun returnToMainActivity(note: Note) {
        val detailIntent = Intent().apply {
            putExtra("editeNote", note)
        }
        setResult(RESULT_OK, detailIntent)
        finish()
    }

    private fun insert(note: Note) {
        executorService.execute {
            mNoteDao.insert(note)
        }
    }
}