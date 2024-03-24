package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menupage)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)
        val Ustawienia: LinearLayout = findViewById(R.id.ustawienia)

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        Ustawienia.setOnClickListener {
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