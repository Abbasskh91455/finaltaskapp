package com.example.database


import android.util.Log
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Connection
import java.sql.ResultSet

object DatabaseHelper {
    private const val URL = "jdbc:mysql://10.0.2.2:3306/alhsn"
    private const val username = "root"
    private const val password = ""

    init {
        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.e("tag", "MySQL JDBC Driver not found")
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, username, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e("tag", "Connection Error: ${e.message}")
            null
        }
    }

    fun executeQuery(query: String): ResultSet? {
        return try {
            val con = getConnection()
            con?.createStatement()?.executeQuery(query)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}