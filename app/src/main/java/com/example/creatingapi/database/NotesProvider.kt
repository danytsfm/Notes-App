package com.example.creatingapi.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns._ID
import androidx.annotation.RequiresApi
import com.example.creatingapi.database.NotesDatabase.Companion.TABLE_NAME

class NotesProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper : NotesDatabase

    override fun onCreate(): Boolean {
       mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY, "notes", NOTES)
        mUriMatcher.addURI(AUTHORITY, "notes/#", NOTES_BY_ID)
        if (context != null){dbHelper = NotesDatabase(context as Context)
        }

        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
       if (mUriMatcher.match(uri) == NOTES_BY_ID){
           val  db : SQLiteDatabase  = dbHelper.writableDatabase
           val affectedLines = db.delete(TABLE_NAME, "$_ID = ?", arrayOf(uri.lastPathSegment))
           db.close()
           context?.contentResolver?.notifyChange(uri, null)
           return affectedLines

       }else{
           throw UnsupportedOperationException("Invalid URI for exclusion")
       }

    }

    override fun getType(uri: Uri): String? = throw UnsupportedOperationException("Not Implemented")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (mUriMatcher.match(uri) == NOTES){
            val  db : SQLiteDatabase  = dbHelper.writableDatabase
            val id = db.insert(TABLE_NAME, null, values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
           db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return insertUri
        }else{
            throw UnsupportedOperationException("Invalid URI for insertion")
        }

    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when{
            mUriMatcher.match(uri) == NOTES -> {
                val  db : SQLiteDatabase  = dbHelper.writableDatabase
                val cursor = db.query(TABLE_NAME, projection, selection, selectionArgs,
                    null, null, sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            mUriMatcher.match(uri) == NOTES_BY_ID ->{
                val  db : SQLiteDatabase  = dbHelper.writableDatabase
                val cursor = db.query(TABLE_NAME, projection, "$_ID = ?",
                    arrayOf(uri.lastPathSegment), null, null, sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else ->{
                throw UnsupportedOperationException("Not Implemented")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if(mUriMatcher.match(uri) == NOTES_BY_ID){
            val  db : SQLiteDatabase  = dbHelper.writableDatabase
            val updatedtLines = db.update(TABLE_NAME, values, "$_ID = ?", arrayOf(uri.lastPathSegment))
          db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return updatedtLines
        }else{
            throw UnsupportedOperationException("Invalid URI for update")
        }
    }

    companion object{
        const val AUTHORITY = "com.example.creatingapi.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI, "notes")

        const val NOTES = 1
        const val NOTES_BY_ID = 2
    }
}