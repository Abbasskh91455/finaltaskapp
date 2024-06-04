package com.example.database

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val userlist = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listview)
        CoroutineScope(Dispatchers.IO).launch {
            fetchdatabase()
        }

    }

    private suspend fun fetchdatabase() {
        val query = "SELECT * FROM user"
        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = DatabaseHelper.getConnection()
            if (connection != null) {
                val statemnet = connection.createStatement()
                resultSet = statemnet.executeQuery(query)

                if (resultSet != null) {
                    while (resultSet.next()) {
                        val id = resultSet.getInt("Id")
                        val fullname = resultSet.getString("Full name")
                        val phonenb = resultSet.getInt("Phone number")
                        val pass = resultSet.getInt("Password")
                        val address = resultSet.getString("Address")
                        val user = "$id  $fullname  $phonenb  $pass  $address"
                        userlist.add(user)
                    }
                    withContext(Dispatchers.Main) {
                        updateUI()
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "fetch error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            Log.e("tag4", "Error excuting", e)
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userlist)
        listView.adapter= adapter
    }
}