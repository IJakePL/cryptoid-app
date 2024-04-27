package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HelpPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.helppage)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val chat: ImageButton = findViewById(R.id.chat)

        chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val about: LinearLayout = findViewById(R.id.faq)
        val author: LinearLayout = findViewById(R.id.kontakt)

        about.setOnClickListener {
            val intent = Intent(this, PomocActivity::class.java)
            startActivity(intent)
        }

        author.setOnClickListener {
            val intent = Intent(this, KontaktActivity::class.java)
            startActivity(intent)
        }
    }
}