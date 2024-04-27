package com.nestnet.nestapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GroupShowActivity : AppCompatActivity() {
    var name = ""
    var count_member = ""
    var count_game = ""
    var count_online = ""
    var group_search = ""
    var group_about = ""
    var group_lang = ""
    var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group)

        val received = intent.getStringExtra("groupid") ?: ""
        FetchDataTask().execute(received)
    }

    private inner class FetchDataTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val receiveds = params[0]
            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/group/search/info")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$receiveds"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    Log.d("test group", response)

                    name = jsonResponse.getString("name")
                    count_member = jsonResponse.getString("count_member")
                    count_game = jsonResponse.getString("count_game")
                    count_online = jsonResponse.getString("count_online")
                    group_search = jsonResponse.getString("group_search")
                    group_about = jsonResponse.getString("group_about")
                    group_lang = jsonResponse.getString("group_lang")
                    date = jsonResponse.getString("date")

                    return true
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return false
                } else {
                    return false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                val datenow = formatMessageDate(date)

                val textView: TextView = findViewById(R.id.groupname)
                textView.text = name

                val textView1: TextView = findViewById(R.id.czlon)
                textView1.text = count_member

                val textView2: TextView = findViewById(R.id.game)
                textView2.text = count_game

                val textView3: TextView = findViewById(R.id.online)
                textView3.text = count_online

                val textView4: TextView = findViewById(R.id.about)
                textView4.text = group_about

                val textView5: TextView = findViewById(R.id.lang)
                textView5.text = group_lang

                val textView6: TextView = findViewById(R.id.date)
                textView6.text = datenow
            } else {
                Toast.makeText(this@GroupShowActivity, "Napotkaliśmy problem! (yu73)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun formatMessageDate(messageDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateTime = LocalDateTime.parse(messageDate, formatter)
        val formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        return formattedDateTime
    }
}