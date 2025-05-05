package com.example.juego_adivinar_numero_aleatorio

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date

class GameDbHelper(context: Context) : SQLiteOpenHelper(context, "game.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS attempts (id INTEGER PRIMARY KEY AUTOINCREMENT, points INTEGER, fails INTEGER, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
        db.execSQL("INSERT INTO attempts (fails, points) VALUES (0, 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS attempts")
        onCreate(db)
    }

    fun modifyAttempt(attempt: Attempt) {
        val db = writableDatabase
        val sql = "UPDATE attempts SET points = ?, fails = ? WHERE id = ?"
        val statement = db.compileStatement(sql)
        statement.bindLong(1, attempt.points.toLong())
        statement.bindLong(2, attempt.fails.toLong())
        statement.bindLong(3, attempt.id.toLong())
        statement.executeUpdateDelete()
        if (attempt.fails >= 5) {
            createAttempt()
        }
        db.close()
    }

    fun getAttempts(): List<Attempt> {
        val attempts = mutableListOf<Attempt>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attempts", null)

        if (cursor.moveToFirst()) {
            do {
                val attempt = Attempt(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    points = cursor.getInt(cursor.getColumnIndexOrThrow("points")),
                    fails = cursor.getInt(cursor.getColumnIndexOrThrow("fails")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                )
                attempts.add(attempt)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return attempts
    }

    fun createAttempt(): Attempt {
        val db = writableDatabase
        db.execSQL("INSERT INTO attempts (fails, points) VALUES (0, 0)")
        db.close()
        return getLastAttempt();
    }

    fun getLastAttempt(): Attempt {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attempts ORDER BY date DESC LIMIT 1", null)
        var attempt: Attempt? = null

        if (cursor.moveToFirst()) {
            val fails = cursor.getInt(cursor.getColumnIndexOrThrow("fails"))
            if (fails < 5) {
                attempt = Attempt(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    points = cursor.getInt(cursor.getColumnIndexOrThrow("points")),
                    fails = cursor.getInt(cursor.getColumnIndexOrThrow("fails")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                )
            } else {
                modifyAttempt(Attempt(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    points = cursor.getInt(cursor.getColumnIndexOrThrow("points")),
                    fails = cursor.getInt(cursor.getColumnIndexOrThrow("fails")),
                    date = Date().toString()
                ))
                attempt = createAttempt()
            }
        }

        cursor.close()
        db.close()
        return attempt!!
    }

    fun getBestAttempt(): Attempt? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attempts ORDER BY points DESC LIMIT 1", null)
        var attempt: Attempt? = null

        if (cursor.moveToFirst()) {
            attempt = Attempt(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                points = cursor.getInt(cursor.getColumnIndexOrThrow("points")),
                fails = cursor.getInt(cursor.getColumnIndexOrThrow("fails")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            )
        }

        cursor.close()
        db.close()
        return attempt
    }

}
