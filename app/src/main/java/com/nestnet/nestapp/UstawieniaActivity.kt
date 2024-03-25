package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader

class UstawieniaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ustawienia)

        var plan = ""
        var centrum_rodzinne = ""
        var connect_account = ""
        var zaproszenia = ""
        var motyw = ""
        var language = ""
        var website = ""
        var chat = ""
        var push = ""

        val file = File(this@UstawieniaActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    plan = jsonObject.getString("plan")
                    centrum_rodzinne = jsonObject.getString("centrum_rodzinne")
                    connect_account = jsonObject.getString("connect_account")
                    zaproszenia = jsonObject.getString("invite")
                    motyw = jsonObject.getString("motyw")
                    website = jsonObject.getString("website")
                    language = jsonObject.getString("language")
                    chat = jsonObject.getString("chat")
                    push = jsonObject.getString("push")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@UstawieniaActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView: TextView = findViewById(R.id.podany_plan)
        textView.text = plan

        val textView1: TextView = findViewById(R.id.podane_centrum_rodzinne)
        textView1.text = centrum_rodzinne

        val textView2: TextView = findViewById(R.id.podane_connect_account)
        textView2.text = connect_account

        val textView3: TextView = findViewById(R.id.zaproszenia)
        textView3.text = zaproszenia

        val textView4: TextView = findViewById(R.id.wyglad)
        textView4.text = motyw

        val textView5: TextView = findViewById(R.id.website)
        textView5.text = website

        val textView6: TextView = findViewById(R.id.language)
        textView6.text = language

        val textView7: TextView = findViewById(R.id.chat)
        textView7.text = chat

        val textView8: TextView = findViewById(R.id.push)
        textView8.text = push

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}