package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

var userid = ""
class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menupage)

        val file = File(this@MenuActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    userid = jsonObject.getString("plan")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

//        switched.setOnClickListener {
//            val jsonObject = JSONObject().apply {
//                put("email", "")
//                put("password", "")
//            }
//
//            val file = File(filesDir, "user.json")
//            val fileWriter = FileWriter(file)
//            fileWriter.use {
//                it.write(jsonObject.toString())
//            }
//
//            OfflineActv().execute(userid)
//            val intent = Intent(this@MenuActivity, SplashActivity::class.java)
//            startActivity(intent)
//        }

        val HomeButton: ImageButton = findViewById(R.id.home)
        val chat: ImageButton = findViewById(R.id.chat)

        chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
            startActivity(intent, bundel)
        }

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
            startActivity(intent, bundel)
        }
    }

    private inner class OfflineActv : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/status/user/change")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user&status=Offline"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }
    }
}