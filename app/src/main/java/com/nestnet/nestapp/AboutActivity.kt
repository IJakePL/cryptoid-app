package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dowiedz_sie_wiecej)

        val rozpocznijButton: Button = findViewById(R.id.dsw_zacznij)
        val powrotButton: Button = findViewById(R.id.dsw_powrot)
        val logButton: TextView = findViewById(R.id.dsw_logowanie)

        rozpocznijButton.setOnClickListener {
            val intent = Intent(this, ZacznijActivity::class.java)
            startActivity(intent)
        }

        logButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        powrotButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}