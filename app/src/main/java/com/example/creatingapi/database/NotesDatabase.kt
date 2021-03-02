package com.example.creatingapi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDatabase(
    context: Context
) : SQLiteOpenHelper(context, "Notesdatabase", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME (" +
                "$_ID    INTEGER NOT NULL PRIMARY KEY," +
                "$COLUMNS_TITLE TEXT NOT NULL," +
                "$COLUMNS_DESC TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       //no implementation
    }

    companion object{
        const val TABLE_NAME : String = "Notes"
        const val COLUMNS_TITLE : String = "title"
        const val COLUMNS_DESC : String = "description"
    }
}