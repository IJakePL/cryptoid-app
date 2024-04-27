package com.nestnet.nestapp

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
                startActivity(intent)
                finish()
            }
        }

        val MenuButton: ImageButton = findViewById(R.id.home_button1)
        val Ustawienia: LinearLayout = findViewById(R.id.ustawienia)
        val Sklep: LinearLayout = findViewById(R.id.sklep)
        val Aktu: LinearLayout = findViewById(R.id.aktu)
        val switched: LinearLayout = findViewById(R.id.switched)
        val ustawkonta: LinearLayout = findViewById(R.id.ustawkonta)
        val aboutapp: LinearLayout = findViewById(R.id.aboutapp)
        val helps: LinearLayout = findViewById(R.id.helps)

        aboutapp.setOnClickListener {
            val intent = Intent(this, OnasActivity::class.java)
            startActivity(intent)
        }

        helps.setOnClickListener {
            val intent = Intent(this, HelpPageActivity::class.java)
            startActivity(intent)
        }

        ustawkonta.setOnClickListener {
            val intent = Intent(this, KontoActivity::class.java)
            startActivity(intent)
        }

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        Sklep.setOnClickListener {
            val intent = Intent(this, SklepActivity::class.java)
            startActivity(intent)
        }

        Aktu.setOnClickListener {
            val intent = Intent(this, AktualizacjaActivity::class.java)
            startActivity(intent)
        }

        Ustawienia.setOnClickListener {
            val intent = Intent(this, UstawieniaActivity::class.java)
            startActivity(intent)
        }

        switched.setOnClickListener {
            val jsonObject = JSONObject().apply {
                put("email", "")
                put("password", "")
            }

            val file = File(filesDir, "user.json")
            val fileWriter = FileWriter(file)
            fileWriter.use {
                it.write(jsonObject.toString())
            }

            OfflineActv().execute(userid)
            val intent = Intent(this@MenuActivity, SplashActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)
        val chat: ImageButton = findViewById(R.id.chat)

        chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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