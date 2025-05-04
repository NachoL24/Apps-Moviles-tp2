package com.example.juego_adivinar_numero_aleatorio

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GameDbHelper(context: Context) : SQLiteOpenHelper(context, "game.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS attempts (id INTEGER PRIMARY KEY AUTOINCREMENT, fails INTEGER)")
        db.execSQL("INSERT INTO attempts (fails) VALUES (0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS attempts")
        onCreate(db)
    }

    fun getFails(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT fails FROM attempts LIMIT 1", null)
        val fails = if (cursor.moveToFirst()) cursor.getInt(0) else {
            db.execSQL("INSERT INTO attempts (fails) VALUES (0)")
            0
        }
        cursor.close()
        return fails
    }

    fun setFails(fails: Int) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT id FROM attempts LIMIT 1", null)
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            db.execSQL("UPDATE attempts SET fails = ? WHERE id = ?", arrayOf(fails, id))
        } else {
            db.execSQL("INSERT INTO attempts (fails) VALUES (?)", arrayOf(fails))
        }
        cursor.close()
    }

    fun resetFails() = setFails(0)
}
