package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil);

        var name = ""
        var money = ""
        var game = ""
        var friends = ""

        val file = File(this@ProfileActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    name = jsonObject.getString("name")
                    money = jsonObject.getString("money_number")
                    game = jsonObject.getString("game_number")
                    friends = jsonObject.getString("friends_number")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.podana_nazwa)
        textView1.text = name
        val textView2: TextView = findViewById(R.id.podane_saldo)
        textView2.text = money + " PLN"
        val textView3: TextView = findViewById(R.id.podani_znajomi)
        textView3.text = friends
        val textView4: TextView = findViewById(R.id.podane_gry)
        textView4.text = game

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}