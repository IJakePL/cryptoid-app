package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class TworcyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tworcy)

        val githubButtonR4ved: LinearLayout = findViewById(R.id.github_r4ved)
        val githubButtonXanto: LinearLayout = findViewById(R.id.github_xantotech)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, OnasActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@TworcyActivity).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@TworcyActivity).toBundle()
            startActivity(intent, bundel)
        }

        githubButtonR4ved.setOnClickListener {
            it.animate().apply {
                duration = 200
                scaleX(0.9f)
                scaleY(0.9f)
                withEndAction {
                    scaleX(1.0f)
                    scaleY(1.0f)
                }
                start()
            }

            val githubUrl = "https://github.com/r4ved"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@TworcyActivity).toBundle()
            startActivity(intent, bundel)
        }

        githubButtonXanto.setOnClickListener {
            it.animate().apply {
                duration = 200
                scaleX(0.9f)
                scaleY(0.9f)
                withEndAction {
                    scaleX(1.0f)
                    scaleY(1.0f)
                }
                start()
            }

            val githubUrl = "https://github.com/XantoTech"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@TworcyActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}