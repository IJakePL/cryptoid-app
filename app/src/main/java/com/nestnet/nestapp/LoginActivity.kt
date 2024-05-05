package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.os.AsyncTask
import android.widget.Button
import android.widget.EditText
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

class LoginActivity : ComponentActivity() {

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button

    var name = ""
    var plan = ""
    var userId = ""
    var emails = ""
    var email_verify = ""
    var push = ""
    var website = ""
    var referral = ""
    var language = ""
    var chat = ""
    var motyw = ""
    var invite = ""
    var connect_account = ""
    var centrum_rodzinne = ""
    var synchro = ""
    var dm_allow = ""
    var dm_unread = ""
    var dm_none = ""
    var friends_number = ""
    var game_number = ""
    var money_number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logowanie)


        val UtworzButton: TextView = findViewById(R.id.lg_utworz)

        UtworzButton.setOnClickListener {
            val intent = Intent(this, ZacznijActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
            startActivity(intent, bundel)
        }


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
                val url = URL("http://fi3.bot-hosting.net:20688/api/v1/find/accounts")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                var key = "e24863b1ad3d8c30363fd085da0ee00e932b6c5ae5e9398a2d525ad765d42b72"

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "email=$email&haslo=$password&apikey=$key"
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
                    email_verify = jsonResponse.getString("email_verify")
                    push = jsonResponse.getString("push")
                    website = jsonResponse.getString("website")
                    language = jsonResponse.getString("language")
                    chat = jsonResponse.getString("chat")
                    motyw = jsonResponse.getString("motyw")
                    invite = jsonResponse.getString("invite")
                    connect_account = jsonResponse.getString("connect_account")
                    centrum_rodzinne = jsonResponse.getString("centrum_rodzinne")
                    synchro = jsonResponse.getString("synchro")
                    dm_allow = jsonResponse.getString("dm_allow")
                    dm_unread = jsonResponse.getString("dm_unread")
                    dm_none = jsonResponse.getString("dm_none")
                    friends_number = jsonResponse.getString("friends")
                    money_number = jsonResponse.getString("money")
                    game_number = jsonResponse.getString("game")

                    val jsonObject = JSONObject().apply {
                        put("email", email)
                        put("password", password)
                        put("name", name)
                        put("email_verify", email_verify)
                        put("plan", plan)
                        put("userId", userId)
                        put("push", push)
                        put("website", website)
                        put("language", language)
                        put("chat", chat)
                        put("motyw", motyw)
                        put("invite", invite)
                        put("connect_account", connect_account)
                        put("centrum_rodzinne", centrum_rodzinne)
                        put("synchro", synchro)
                        put("dm_allow", dm_allow)
                        put("dm_unread", dm_unread)
                        put("dm_none", dm_none)
                        put("friends_number", friends_number)
                        put("money_number", money_number)
                        put("game_number", game_number)
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
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                Toast.makeText(this@LoginActivity, "Napotkaliśmy problem! (ars18)", Toast.LENGTH_SHORT).show()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
                startActivity(intent, bundel)
            }
            finish()
        }
    }
}
