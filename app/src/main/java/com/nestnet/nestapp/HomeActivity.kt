package com.nestnet.nestapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import com.nestnet.nestapp.utils.CheckNetwork

class HomeActivity : ComponentActivity() {

    private var wasConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        if (wasConnected) {
            wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)
        }

        wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)

        // podany email
        val textView1: TextView = findViewById(R.id.podany_email)
        val receivedEmail1 = intent.getStringExtra("EMAIL")
        textView1.text = "EMAIL: " + receivedEmail1

        // podane haslo
        val textView2: TextView = findViewById(R.id.podany_plan)
        val receivedEmail2 = intent.getStringExtra("PLAN")
        textView2.text = "PLAN: " + receivedEmail2

        // podana nazwa
        val textView3: TextView = findViewById(R.id.podana_nazwa)
        val receivedEmail3 = intent.getStringExtra("NAME")
        textView3.text = "NAZWA: " + receivedEmail3

        // podany user id
        val textView4: TextView = findViewById(R.id.podane_id)
        val receivedEmail4 = intent.getStringExtra("USERID")
        textView4.text = "ID: " + receivedEmail4

        val logButton: Button = findViewById(R.id.logout)

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