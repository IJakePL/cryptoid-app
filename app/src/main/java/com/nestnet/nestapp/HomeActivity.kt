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

class HomeActivity : ComponentActivity() {

    private var wasConnected = false

    var name = ""
    var plan = ""
    var userId = ""
    var emails = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        val file = File(this@HomeActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    emails = jsonObject.getString("email")
                    name = jsonObject.getString("name")
                    plan = jsonObject.getString("plan")
                    userId = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.podany_email)
        textView1.text = "EMAIL: " + emails

        val textView2: TextView = findViewById(R.id.podany_plan)
        textView2.text = "PLAN: " + plan

        val textView3: TextView = findViewById(R.id.podana_nazwa)
        textView3.text = "NAZWA: " + name

        val textView4: TextView = findViewById(R.id.podane_id)
        textView4.text = "ID: " + userId

        if (wasConnected) {
            wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)
        }

        wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)

        val logButton: Button = findViewById(R.id.logout)
        val MenuButton: ImageButton = findViewById(R.id.search)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        logButton.setOnClickListener {
            val jsonObject = JSONObject().apply {
                put("email", "")
                put("password", "")
            }

            val file = File(filesDir, "user.json")
            val fileWriter = FileWriter(file)
            fileWriter.use {
                it.write(jsonObject.toString())
            }

            val intent = Intent(this@HomeActivity, SplashActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val isConnected = CheckNetwork.isWifiConnected(this@HomeActivity)
        if (isConnected && !wasConnected) {
            showToast("Połączono z Wi-Fi")
            wasConnected = true
        } else if (!isConnected && wasConnected) {
            showToast("Utracono połączenie Wi-Fi")
            wasConnected = false
        } else if (isConnected && !wasConnected && wasConnected != CheckNetwork.isWifiConnected(this@HomeActivity)) {
            showToast("Wznowiono połączenie Wi-Fi")
            wasConnected = true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}