package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class ConnectAccountMethodActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_account_method)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this@ConnectAccountMethodActivity, ConnectAccountActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ConnectAccountMethodActivity).toBundle()
            startActivity(intent, bundel)
        }

        val MethodButton: LinearLayout = findViewById(R.id.discord)

        MethodButton.setOnClickListener {
            val intent = Intent(this@ConnectAccountMethodActivity, ConnectAccountDiscordActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ConnectAccountMethodActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}
