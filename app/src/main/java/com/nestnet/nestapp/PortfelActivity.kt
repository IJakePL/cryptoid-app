package com.nestnet.nestapp

import Task
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PortfelActivity : AppCompatActivity() {
    var name = ""
    var user_ids = ""
    var money = ""
    var emails = ""
    var email_verify = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.portfel)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val Profile: ImageButton = findViewById(R.id.profile)

        Profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val Email: TextView  = findViewById(R.id.email)

        Email.setOnClickListener {
            val intent = Intent(this, ChangeEmailActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val file = File(this@PortfelActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    user_ids = jsonObject.getString("userId")
                    money = jsonObject.getString("money_number")
                    emails = jsonObject.getString("email")
                    email_verify = jsonObject.getString("email_verify")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@PortfelActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.podane_saldo)
        textView1.text = money + "zł"

        val textView2: TextView = findViewById(R.id.podany_email)
        textView2.text = emails

        val textView3: TextView = findViewById(R.id.status)
        textView3.text = email_verify
    }
}