package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.graphics.Color
import android.os.AsyncTask
import android.text.TextWatcher
import android.util.Log
import androidx.activity.ComponentActivity
import android.util.Patterns
import android.widget.Toast
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ZacznijActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zacznij)

        val email_input: EditText = findViewById(R.id.email_input)
        val referral_input: EditText = findViewById(R.id.referral_input)
        val name_input: EditText = findViewById(R.id.name_input)
        val password_input: EditText = findViewById(R.id.password_input)
        val password_conf_input: EditText = findViewById(R.id.password_conf_input)

        val dswButton: ImageButton = findViewById(R.id.lg_back)
        val logButton: Button = findViewById(R.id.zacznij)

        class SendDataToServerTask(private val context: Context, private val nazwa: String, private val email: String, private val haslo: String, private val referral: String) : AsyncTask<Void, Void, Boolean>() {
            override fun doInBackground(vararg params: Void?): Boolean {
                return try {
                    val url = URL("http://fi3.bot-hosting.net:20688/api/create/accounts")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.doOutput = true

                    val payload = "nazwa=$nazwa&email=$email&haslo=$haslo&referral=$referral"
                    val outputStream = OutputStreamWriter(connection.outputStream)
                    outputStream.write(payload)
                    outputStream.flush()

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        return true
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        return false
                    } else {
                        return false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }

            override fun onPostExecute(result: Boolean) {
                if (result) {
                    try {
                        val jsonObject = JSONObject().apply {
                            put("email", email)
                            put("password", haslo)
                        }

                        val file = File(context.filesDir, "user.json")
                        val fileWriter = FileWriter(file)
                        fileWriter.use {
                            it.write(jsonObject.toString())
                        }

                        val intent = Intent(this@ZacznijActivity, SplashActivity::class.java)
                        val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ZacznijActivity).toBundle()
                        startActivity(intent, bundel)
                    } catch (e: Exception) {
                        println("Wystąpił błąd: $e")
                    }
                    Toast.makeText(context, "Twoje konto zostało utworzone.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@ZacznijActivity, LoginActivity::class.java)
                    val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ZacznijActivity).toBundle()
                    startActivity(intent, bundel)
                    Toast.makeText(context, "Konto o podanym emailu już istnieje!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun register(email: String, referral: String, nazwa: String, haslo: String, hasloconf: String) {
            if (haslo == hasloconf) {
                SendDataToServerTask(this, nazwa, email, haslo, referral).execute()
            } else {
                Toast.makeText(this, "Hasła muszą być takie same!", Toast.LENGTH_SHORT).show()
            }
        }

        logButton.setOnClickListener {
            val email = email_input.text.toString()
            val referral = if (referral_input.text.toString().isNotEmpty()) {
                referral_input.text.toString()
            } else {
                "@null"
            }
            val nazwa = name_input.text.toString()
            val haslo = password_input.text.toString()
            val hasloconf = password_conf_input.text.toString()

            register(email, referral, nazwa, haslo, hasloconf)
        }

        dswButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ZacznijActivity).toBundle()
            startActivity(intent, bundel)
        }
    }
}
