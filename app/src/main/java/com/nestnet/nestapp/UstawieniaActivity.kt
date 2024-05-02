package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class UstawieniaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ustawienia)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@UstawieniaActivity).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@UstawieniaActivity).toBundle()
            startActivity(intent, bundel)
        }

    }
}