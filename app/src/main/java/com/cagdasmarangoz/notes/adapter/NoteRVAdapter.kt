package com.cagdasmarangoz.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cagdasmarangoz.notes.R
import com.cagdasmarangoz.notes.database.Note

class NoteRVAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteDeleteClick: (Note) -> Unit,
) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {
    private var allNote = ArrayList<Note>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTV = itemView.findViewById<TextView>(R.id.idTVNoteTitle)
        val timeTV = itemView.findViewById<TextView>(R.id.idTVTimeStamp)
        val deleteTV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.setText(allNote.get(position).noteTitle)
        holder.timeTV.setText("Last Update" + allNote.get(position).timeStamp)
        holder.deleteTV.setOnClickListener {
            onNoteDeleteClick(allNote[position])
        }
        holder.itemView.setOnClickListener {
            onNoteClick(allNote[position])
        }
    }

    override fun getItemCount(): Int {
        return allNote.size
    }

    fun updateList(newList: List<Note>) {
        allNote.clear()
        allNote.addAll(newList)
        notifyDataSetChanged()
    }

    fun setData(newData: List<Note>) {
        allNote = newData as ArrayList<Note>
        notifyDataSetChanged()
    }
}