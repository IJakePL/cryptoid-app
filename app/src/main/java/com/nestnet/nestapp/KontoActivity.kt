package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader

class KontoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.konto)

        var name = ""
        var emails = ""

        val file = File(this@KontoActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    emails = jsonObject.getString("email")
                    name = jsonObject.getString("name")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@KontoActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.podany_email)
        textView1.text = emails

        val textView2: TextView = findViewById(R.id.podana_nazwa)
        textView2.text = name

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, UstawieniaActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}