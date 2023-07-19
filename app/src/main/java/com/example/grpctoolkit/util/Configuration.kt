package com.example.grpctoolkit.util

import com.example.grpctoolkit.GRPCApplication
import com.example.grpctoolkit.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Configuration {
    private val configMap = mutableMapOf<String, String>()

    init {
        try {
            val inputStream =
                GRPCApplication.applicationContext().resources.openRawResource(R.raw.config)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                val keyValuePair = line.split("=")
                if (keyValuePair.size == 2) {
                    val key = keyValuePair[0].trim()
                    val value = keyValuePair[1].trim()
                    configMap[key] = value
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getServerIp(): String? = configMap["server_ip"]

    fun getServerPort(): Int = configMap["server_port"]?.toInt()!!

}
