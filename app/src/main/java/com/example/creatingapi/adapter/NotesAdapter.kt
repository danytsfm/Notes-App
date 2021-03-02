package com.example.creatingapi.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.creatingapi.NoteClicksListener
import com.example.creatingapi.R
import com.example.creatingapi.database.NotesDatabase.Companion.COLUMNS_DESC
import com.example.creatingapi.database.NotesDatabase.Companion.COLUMNS_TITLE


class NotesAdapter(private var listener: NoteClicksListener) : RecyclerView.Adapter<NotesViewHolder>(){

    private var mCursor: Cursor? =  null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
            NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false))

    override fun getItemCount(): Int = if (mCursor != null) mCursor?.count as Int else 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)
       holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(COLUMNS_TITLE) as Int)
        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(COLUMNS_DESC) as Int)
        holder.btnDelete.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteDeleteItem(mCursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener { listener.noteClickItem(mCursor as Cursor) }

    }

     fun setCursor(newCursor : Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }

}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val noteTitle = itemView.findViewById(R.id.note_title) as TextView
    val noteDescription = itemView.findViewById(R.id.note_description) as TextView
    val btnDelete = itemView.findViewById(R.id.btn_note_remove) as Button
}