package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class OnasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutapp)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivity).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivity).toBundle()
            startActivity(intent, bundel)
        }

        val about: LinearLayout = findViewById(R.id.about)
        val author: LinearLayout = findViewById(R.id.author)
        val privacypolicy: LinearLayout = findViewById(R.id.privacypolicy)

        about.setOnClickListener {
            val intent = Intent(this, OnasActivityzwei::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivity).toBundle()
            startActivity(intent, bundel)
        }

        author.setOnClickListener {
            val intent = Intent(this, TworcyActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivity).toBundle()
            startActivity(intent, bundel)
        }

        privacypolicy.setOnClickListener {
            val intent = Intent(this, PrivacePolicyActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@OnasActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}