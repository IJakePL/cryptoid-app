package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

var userid = ""
class MenuActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menupage)

        val file = File(this@MenuActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    userid = jsonObject.getString("plan")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

//        switched.setOnClickListener {
//            val jsonObject = JSONObject().apply {
//                put("email", "")
//                put("password", "")
//            }
//
//            val file = File(filesDir, "user.json")
//            val fileWriter = FileWriter(file)
//            fileWriter.use {
//                it.write(jsonObject.toString())
//            }
//
//            OfflineActv().execute(userid)
//            val intent = Intent(this@MenuActivity, SplashActivity::class.java)
//            startActivity(intent)
//        }

        val HomeButton: ImageButton = findViewById(R.id.home)
        val chat: ImageButton = findViewById(R.id.chat)

        chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
            startActivity(intent, bundel)
        }

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MenuActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}