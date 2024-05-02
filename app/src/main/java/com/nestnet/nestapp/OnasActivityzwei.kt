package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class OnasActivityzwei : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onas)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, OnasActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivityzwei).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivityzwei).toBundle()
            startActivity(intent, bundel)
        }
    }
}