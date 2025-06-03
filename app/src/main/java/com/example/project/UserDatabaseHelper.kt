package com.example.project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userType TEXT NOT NULL,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                cnic TEXT NOT NULL UNIQUE,
                contact TEXT NOT NULL UNIQUE,
                location TEXT NOT NULL,
                expertise TEXT NOT NULL,
                experience TEXT NOT NULL,
                gender TEXT NOT NULL,
                imageUri TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun registerWorker(
        name: String,
        email: String,
        password: String,
        cnic: String,
        contact: String,
        location: String,
        expertise: String,
        experience: String,
        gender: String,
        imageUri: String
    ) {
        val db = writableDatabase
        val insertQuery = """
            INSERT INTO users (userType, name, email, password, cnic, contact, location, expertise, experience, gender, imageUri)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()
        val stmt = db.compileStatement(insertQuery)

        stmt.bindString(1, "worker")
        stmt.bindString(2, name)
        stmt.bindString(3, email)
        stmt.bindString(4, password)
        stmt.bindString(5, cnic)
        stmt.bindString(6, contact)
        stmt.bindString(7, location)
        stmt.bindString(8, expertise)
        stmt.bindString(9, experience)
        stmt.bindString(10, gender)
        stmt.bindString(11, imageUri)

        stmt.executeInsert()
        db.close()
    }

    fun getAllWorkers(): List<User> {
        val db = readableDatabase
        val workerList = mutableListOf<User>()

        // Using COLLATE NOCASE to make it case-insensitive
        val cursor = db.rawQuery("SELECT * FROM users WHERE userType = 'worker' COLLATE NOCASE", null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    userType = cursor.getString(cursor.getColumnIndexOrThrow("userType")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cnic = cursor.getString(cursor.getColumnIndexOrThrow("cnic")),
                    contact = cursor.getString(cursor.getColumnIndexOrThrow("contact")),
                    location = cursor.getString(cursor.getColumnIndexOrThrow("location")),
                    expertise = cursor.getString(cursor.getColumnIndexOrThrow("expertise")),
                    experience = cursor.getString(cursor.getColumnIndexOrThrow("experience")),
                    gender = cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("imageUri"))
                )
                workerList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return workerList
    }

    companion object {
        const val DATABASE_NAME = "userDatabase.db"
        const val DATABASE_VERSION = 1
    }
}