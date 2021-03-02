package com.example.creatingapi

import android.database.Cursor

interface NoteClicksListener {
    fun noteClickItem(cursor: Cursor)
    fun noteDeleteItem(cursor: Cursor?)
}