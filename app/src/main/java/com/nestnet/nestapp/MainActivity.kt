package com.nestnet.nestapp

import android.app.ActivityOptions
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
        val logButton: Button = findViewById(R.id.wel_logowanie)

        rozpocznijButton.setOnClickListener {
            val intent = Intent(this, ZacznijActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            startActivity(intent, bundel)
        }

        logButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}
