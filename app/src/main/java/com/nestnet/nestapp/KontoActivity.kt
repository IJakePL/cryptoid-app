package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader

class KontoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.konto)

        var name = ""
        var emails = ""

        val file = File(this@KontoActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    emails = jsonObject.getString("email")
                    name = jsonObject.getString("name")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@KontoActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }

        val polaczonekonta: LinearLayout = findViewById(R.id.polaczonekonta)

        polaczonekonta.setOnClickListener {
            val intent = Intent(this, ConnectAccountActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }

        val NameButton: LinearLayout = findViewById(R.id.name)

        NameButton.setOnClickListener {
            val intent = Intent(this, ChangeNameActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }

        val EmailButton: LinearLayout = findViewById(R.id.email)

        EmailButton.setOnClickListener {
            val intent = Intent(this, ChangeEmailActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }

        val PassButton: LinearLayout = findViewById(R.id.pass)

        PassButton.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@KontoActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}