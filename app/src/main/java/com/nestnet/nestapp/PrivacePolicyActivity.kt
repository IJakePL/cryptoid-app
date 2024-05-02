package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PrivacePolicyActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.privacepolicy)

                val MenuButton: ImageButton = findViewById(R.id.home_button1)

                MenuButton.setOnClickListener {
                        val intent = Intent(this, OnasActivity::class.java)
                        val bundel = ActivityOptions.makeSceneTransitionAnimation(this@PrivacePolicyActivity).toBundle()
                        startActivity(intent, bundel)
                }

                val HomeButton: ImageButton = findViewById(R.id.home)

                HomeButton.setOnClickListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        val bundel = ActivityOptions.makeSceneTransitionAnimation(this@PrivacePolicyActivity).toBundle()
                        startActivity(intent, bundel)
                }
        }
}