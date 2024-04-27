package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.os.AsyncTask
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SelectGroupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_group)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val Social: LinearLayout = findViewById(R.id.social)
        val Private: LinearLayout = findViewById(R.id.privates)

        Social.setOnClickListener {
            val intent = Intent(this, CreateGroupActivity::class.java)
            intent.putExtra("GROUP", "social")
            startActivity(intent)
        }

        Private.setOnClickListener {
            val intent = Intent(this, CreateGroupActivity::class.java)
            intent.putExtra("GROUP", "private")
            startActivity(intent)
        }
    }
}
