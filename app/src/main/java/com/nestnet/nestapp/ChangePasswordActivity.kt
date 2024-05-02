package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
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
import java.io.FileReader
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ChangePasswordActivity : ComponentActivity() {

    lateinit var PassInput: EditText
    lateinit var NewPassInput: EditText
    lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, KontoActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangePasswordActivity).toBundle()
            startActivity(intent, bundel)
        }

        var acctpass = ""
        var name = ""

        val file = File(this@ChangePasswordActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    acctpass = jsonObject.getString("password")
                    name = jsonObject.getString("name")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        PassInput = findViewById(R.id.pass_input)
        NewPassInput = findViewById(R.id.new_pass_input)
        loginBtn = findViewById(R.id.change_save)

        loginBtn.setOnClickListener {
            val NewPass = NewPassInput.text.toString()
            val Pass = PassInput.text.toString()

            if (Pass.isNotEmpty() && Pass.isNotEmpty()) {
                LoginTask().execute(NewPass, Pass, acctpass, name)
            } else {
                if (Pass.isEmpty()) {
                    PassInput.error = "Musisz wypełnić te pole!"
                }
                if (Pass.isEmpty()) {
                    PassInput.error = "Musisz wypełnić te pole!"
                }

                Toast.makeText(this, "Musisz podać nazwę konta!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val pass = params[1]
            val name = params[3]
            val pass_actually = params[2]
            val newpass = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/change/password/account")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "pass=$pass&pass_acctualy=$pass_actually&newpass=$newpass&name=$name"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    println("Odpowiedź serwera: $response")

                    val file = File(filesDir, "user.json")
                    val jsonString = file.readText()
                    val jsonObject = JSONObject(jsonString)

                    jsonObject.put("password", newpass)

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
                val intent = Intent(this@ChangePasswordActivity, KontoActivity::class.java)
                Toast.makeText(this@ChangePasswordActivity, "Zapisano nowe hasło!", Toast.LENGTH_SHORT).show()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangePasswordActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                Toast.makeText(this@ChangePasswordActivity, "Napotkaliśmy problem! (ht42)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
