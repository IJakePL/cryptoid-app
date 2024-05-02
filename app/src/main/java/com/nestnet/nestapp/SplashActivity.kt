package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
    var email_verify = ""
    var push = ""
    var website = ""
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
    var id_notify = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var TAG = "FCM notification"

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            id_notify = token
            Log.d(TAG, token)
        })

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
                            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                            startActivity(intent, bundel)
                            finish()
                        }
                    }, 3000)
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                    startActivity(intent, bundel)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        } else {
            val jsonObject = JSONObject().apply {
                put("email", "")
                put("email_verify", "")
                put("password", "")
                put("name", "")
                put("referral", "")
                put("userId", "")
                put("plan", "")
                put("website", "")
                put("language", "")
                put("chat", "")
                put("motyw", "")
                put("invite", "")
                put("connect_account", "")
                put("centrum_rodzinne", "")
                put("push", "")
                put("synchro", "")
                put("dm_allow", "")
                put("dm_unread", "")
                put("dm_none", "")
                put("friends_number", "")
                put("money_number", "")
                put("game_number", "")
            }

            try {
                val fileWriter = FileWriter(file)
                fileWriter.use {
                    it.write(jsonObject.toString())
                }
                println("Plik user.json został utworzony.")

                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Wystąpił błąd podczas tworzenia pliku user.json: ${e.message}")

                val intent = Intent(this@SplashActivity, SplashActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                startActivity(intent, bundel)
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
                        put("email_verify", email_verify)
                        put("name", name)
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
                        put("id_notify", id_notify)
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
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                finish()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                finish()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                startActivity(intent, bundel)
            }
            finish()
        }
    }
}
