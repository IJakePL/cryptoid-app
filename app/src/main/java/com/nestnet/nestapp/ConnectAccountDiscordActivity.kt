package com.nestnet.nestapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ConnectAccountDiscordActivity : ComponentActivity() {

    var name = ""
    var userid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discord_connect)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, ConnectAccountMethodActivity::class.java)
            startActivity(intent)
        }

        val file = File(this@ConnectAccountDiscordActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    userid = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ConnectAccountDiscordActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        ConnectAccountTask().execute(userid)
    }

    inner class ConnectAccountTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            val userid = params[0] ?: return ""

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/account_connect/user/secret")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$userid"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    name = jsonResponse.getString("secret")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return name
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val textView1: TextView = findViewById(R.id.yourid)
            textView1.text = result
        }
    }
}
