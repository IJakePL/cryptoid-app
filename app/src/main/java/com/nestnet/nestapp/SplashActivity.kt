package com.nestnet.nestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class SplashActivity : AppCompatActivity() {
    var name = ""
    var plan = ""
    var userId = ""
    var emails = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val file = File(this@SplashActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    val emails = jsonObject.getString("email")
                    val password = jsonObject.getString("password")

                    Handler().postDelayed({
                        if (emails.isNotEmpty() && password.isNotEmpty()) {
                            FetchDataTask().execute(emails, password)
                        } else {
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }, 3000)
                } else {
                    // Jeśli plik jest pusty, uruchom MainActivity
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            // Jeśli plik nie istnieje, utwórz go i uruchom MainActivity
            val jsonObject = JSONObject().apply {
                put("email", "")
                put("password", "")
            }

            try {
                val fileWriter = FileWriter(file)
                fileWriter.use {
                    it.write(jsonObject.toString())
                }
                println("Plik user.json został utworzony.")

                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Wystąpił błąd podczas tworzenia pliku user.json: ${e.message}")

                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private inner class FetchDataTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val email = params[0]
            val password = params[1]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/find/accounts")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "email=$email&haslo=$password"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    println("Odpowiedź serwera: $response")
                    val jsonResponse = JSONObject(response)

                    name = jsonResponse.getString("name")
                    plan = jsonResponse.getString("plan")
                    userId = jsonResponse.getString("user_id")
                    emails = jsonResponse.getString("email")

                    return true
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return false
                } else {
                    return false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)

                intent.putExtra("EMAIL", emails)
                intent.putExtra("USERID", userId)
                intent.putExtra("PLAN", plan)
                intent.putExtra("NAME", name)

                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}
