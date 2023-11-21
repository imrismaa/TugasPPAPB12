package com.example.tugasppapb12.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasppapb12.database.Note
import com.example.tugasppapb12.databinding.ItemNoteBinding

typealias OnClickNote = (Note) -> Unit
class NoteAdapter (
    private var listNote: List<Note>,
    private val onClickNote: OnClickNote):
    RecyclerView.Adapter<NoteAdapter.ItemNoteViewHolder>(){

    private var onClickDeleteListener: ((Note) -> Unit)? = null

    fun setOnClickDeleteListener(listener: (Note) -> Unit) {
        onClickDeleteListener = listener
    }

    inner class ItemNoteViewHolder(private val binding: ItemNoteBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(note: Note){
            with(binding){
                textTitle.text = note.title

                itemView.setOnClickListener {
                    onClickNote(note)
                }

                btnDelete.setOnClickListener {
                    onClickDeleteListener?.invoke(note)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ItemNoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ItemNoteViewHolder, position: Int) {
        holder.bind(listNote[position])
    }
}