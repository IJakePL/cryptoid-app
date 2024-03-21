package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)

        val rozpocznijButton: Button = findViewById(R.id.wel_rozpocznij)
        val dswButton: Button = findViewById(R.id.wel_dsw)
        val logButton: TextView = findViewById(R.id.wel_logowanie)

        rozpocznijButton.setOnClickListener {
            val intent = Intent(this, ZacznijActivity::class.java)
            startActivity(intent)
        }

        logButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        dswButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}
