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
        val cursor = db.rawQuery("SELECT fails FROM attempts WHERE id = 1", null)
        return if (cursor.moveToFirst()) cursor.getInt(0) else 0
    }

    fun setFails(fails: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE attempts SET fails = ? WHERE id = 1", arrayOf(fails))
    }

    fun resetFails() = setFails(0)
}