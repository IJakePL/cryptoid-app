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

class ChangeNameActivity : ComponentActivity() {

    lateinit var NameInput: EditText
    lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_name)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, KontoActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeNameActivity).toBundle()
            startActivity(intent, bundel)
        }

        var name = ""

        val file = File(this@ChangeNameActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    name = jsonObject.getString("name")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ChangeNameActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeNameActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

        val textView: EditText = findViewById(R.id.nazwa_input)
        textView.setText(name)

        NameInput = findViewById(R.id.nazwa_input)
        loginBtn = findViewById(R.id.change_save)

        loginBtn.setOnClickListener {
            val Names = NameInput.text.toString()

            if (Names.isNotEmpty()) {
                LoginTask().execute(Names, name)
            } else {
                if (Names.isEmpty()) {
                    NameInput.error = "Musisz wypełnić te pole!"
                }

                Toast.makeText(this, "Musisz podać nazwę konta!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val name = params[0]
            val name_actually = params[1]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/change/name/account")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "name=$name&name_acctualy=$name_actually"
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

                    jsonObject.put("name", name)

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
                val intent = Intent(this@ChangeNameActivity, ChangeNameActivity::class.java)
                Toast.makeText(this@ChangeNameActivity, "Zapisano nazwę użytkownika", Toast.LENGTH_SHORT).show()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChangeNameActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                Toast.makeText(this@ChangeNameActivity, "Napotkaliśmy problem! (wr67)", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
