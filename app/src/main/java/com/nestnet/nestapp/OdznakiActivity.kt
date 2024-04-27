package com.nestnet.nestapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import com.nestnet.nestapp.utils.CheckNetwork
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class OdznakiActivity : ComponentActivity() {

    private var wasConnected = false

    var name = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.odzanki)

        val file = File(this@OdznakiActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    name = jsonObject.getString("name")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@OdznakiActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val MenuButton: ImageButton = findViewById(R.id.search)
        val BackButton: ImageButton = findViewById(R.id.home_button1)
        val HomeButton: ImageButton = findViewById(R.id.home)
        val Profile: ImageButton = findViewById(R.id.profile)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        BackButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val textView2: TextView = findViewById(R.id.podana_nazwa)
        textView2.text = name

        Profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}