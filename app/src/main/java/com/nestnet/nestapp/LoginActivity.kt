package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.os.AsyncTask
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : ComponentActivity() {

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logowanie)

        // click
        val powrotButton: ImageButton = findViewById(R.id.lg_back)

        powrotButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // input
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_press)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                LoginTask().execute(email, password)
            } else {
                if (email.isEmpty()) {
                    emailInput.error = "Pole e-mail nie może być puste"
                }
                if (password.isEmpty()) {
                    passwordInput.error = "Pole hasło nie może być puste"
                }

                Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
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

                    val jsonObject = JSONObject().apply {
                        put("email", email)
                        put("password", password)
                    }

                    val file = File(filesDir, "user.json")
                    val fileWriter = FileWriter(file)
                    fileWriter.use {
                        it.write(jsonObject.toString())
                    }

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
                val intent = Intent(this@LoginActivity, SplashActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}
