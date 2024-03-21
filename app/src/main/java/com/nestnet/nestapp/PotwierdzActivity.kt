package com.nestnet.nestapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PotwierdzActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.potwierdz_zacznij)

        val dswButton: ImageButton = findViewById(R.id.zjt_back)
        val logButton: Button = findViewById(R.id.zacznij_kont)

        // podany email
        val textView1: TextView = findViewById(R.id.podany_email)
        val receivedEmail1 = intent.getStringExtra("EMAIL")
        textView1.text = receivedEmail1

        // podane haslo
        val textView2: TextView = findViewById(R.id.podane_haslo)
        val receivedEmail2 = intent.getStringExtra("HASŁO")
        textView2.text = receivedEmail2

        // podane nazwa
        val textView3: TextView = findViewById(R.id.podana_nazwa)
        val receivedEmail3 = intent.getStringExtra("NAZWA")
        textView3.text = receivedEmail3

        val email: String = receivedEmail1!!
        val haslo: String = receivedEmail2!!
        val nazwa: String = receivedEmail3!!

        class SendDataToServerTask(private val context: Context, private val nazwa: String, private val email: String, private val haslo: String) : AsyncTask<Void, Void, Boolean>() {

            override fun doInBackground(vararg params: Void?): Boolean {
                return try {
                    val url = URL("http://fi3.bot-hosting.net:20688/api/create/accounts")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.doOutput = true

                    val payload = "nazwa=$nazwa&email=$email&haslo=$haslo"
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

                        val intent = Intent(this@PotwierdzActivity, SplashActivity::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        println("Wystąpił błąd: $e")
                    }
                    Toast.makeText(context, "Twoje konto zostało utworzone.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@PotwierdzActivity, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(context, "Konto o podanym emailu już istnieje!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun register() {
            SendDataToServerTask(this, nazwa, email, haslo).execute()
        }

        logButton.setOnClickListener {
            register()
        }

        dswButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
