package com.example.creatingapi

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.fragment.app.FragmentManager
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creatingapi.adapter.NotesAdapter
import com.example.creatingapi.database.NotesDatabase.Companion.COLUMNS_TITLE
import com.example.creatingapi.database.NotesProvider.Companion.URI_NOTES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var rv : RecyclerView
    lateinit var fab : FloatingActionButton
    lateinit var adapter : NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab_notes)
        fab.setOnClickListener {
            NotesDetailFragments().show(supportFragmentManager, "dialog")
        }
        rv = findViewById(R.id.rv_notes)
        adapter = NotesAdapter(object : NoteClicksListener {
            override fun noteClickItem(cursor: Cursor) {
                val id = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment = NotesDetailFragments.newInstance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            override fun noteDeleteItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null, null)
            }

        })
        adapter.setHasStableIds(true)

        rv = findViewById(R.id.rv_notes)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null, null, COLUMNS_TITLE)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null){ adapter.setCursor(data)}
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }


}