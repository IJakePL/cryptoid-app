package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.AsyncTask
import android.util.Log
import android.util.Patterns
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

class ChangeEmailActivity : ComponentActivity() {

    lateinit var NameInput: EditText
    lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_email)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, KontoActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeEmailActivity).toBundle()
            startActivity(intent, bundel)
        }

        var email = ""

        val file = File(this@ChangeEmailActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    email = jsonObject.getString("email")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ChangeEmailActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeEmailActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

        fun isEmailValid(email: CharSequence?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        val textView: EditText = findViewById(R.id.email_input)
        textView.setText(email)

        val textView1: TextView = findViewById(R.id.info)
        textView1.setText("Twoje obecne dane kontaktowe to " + email + ". Czy czujesz, że nadszedł czas na odświeżenie ich i wprowadzenie nowych? Jesteś gotowy na zmianę?")

        NameInput = findViewById(R.id.email_input)
        loginBtn = findViewById(R.id.change_save)

        loginBtn.setOnClickListener {
            val Email = NameInput.text.toString()
            if (isEmailValid(Email)) {
                if (Email.isNotEmpty()) {
                    LoginTask().execute(Email, email)
                } else {
                    Toast.makeText(this, "Nieprawidłowy adres e-mail", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Email.isEmpty()) {
                    NameInput.error = "Musisz wypełnić te pole!"
                }
                Toast.makeText(this, "Musisz podać adres e-mail!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val email = params[0]
            val email_actually = params[1]

            Log.d("ChangeEmailActivity", "Rozpoczęto zadanie doInBackground")

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/v1/change/email/account")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                var key = "e24863b1ad3d8c30363fd085da0ee00e932b6c5ae5e9398a2d525ad765d42b72"

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "email=$email&email_acctualy=$email_actually&apikey=$key"
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

                    jsonObject.put("email", email)

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
                val intent = Intent(this@ChangeEmailActivity, ChangeEmailActivity::class.java)
                Toast.makeText(this@ChangeEmailActivity, "Zapisano adres e-mail!", Toast.LENGTH_SHORT).show()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeEmailActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                Toast.makeText(this@ChangeEmailActivity, "Napotkaliśmy problem! (er13)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
