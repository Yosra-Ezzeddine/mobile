package com.example.afinal

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val userList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)

        // Start the database operation
        CoroutineScope(Dispatchers.IO).launch {
            fetchDataFromDatabase()
        }
    }

    private suspend fun fetchDataFromDatabase() {
        val query = "SELECT * FROM app"
        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = DataBaseHelper.getConnection()
            if (connection != null) {
                val statement = connection.createStatement()
                resultSet = statement.executeQuery(query)

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Log.d("MainActivity", "Processing row")
                        val id = resultSet.getInt("id")
                        val firstname = resultSet.getString("first name")
                        val phone = resultSet.getInt("phone")
                        val user = "$id       $firstname      $phone"
                        userList.add(user)
                    }

                    withContext(Dispatchers.Main) {
                        updateUI()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Connection failed", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error executing query", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Error executing query", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                resultSet?.close()
                connection?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        listView.adapter = adapter
    }
}