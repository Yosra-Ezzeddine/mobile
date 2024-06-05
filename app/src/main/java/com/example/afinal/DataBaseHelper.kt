package com.example.afinal

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

object DataBaseHelper {
    private const val DB_URL = "jdbc:mysql://10.0.2.2:3306/mobile"
    private const val USER = "root"
    private const val PASS = ""
    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(DB_URL, USER, PASS).also {
                Log.d("tag1", "Database connection established")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e("tag2", "Error establishing connection: ${e.message}")
            null
        }
    }

    fun executeQuery(query: String): ResultSet? {
        Log.d("tag3", "Executing query: $query")
        return try {
            val con = getConnection()
            con?.createStatement()?.executeQuery(query)?.also {
                Log.d("tag4", "Query executed successfully")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e("tag5", "Error executing query: ${e.message}")
            null
        }
    }
}