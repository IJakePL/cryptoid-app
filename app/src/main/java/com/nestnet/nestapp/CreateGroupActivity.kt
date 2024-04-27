package com.nestnet.nestapp

import android.content.Intent
import android.os.Bundle
import android.os.AsyncTask
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
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
import java.net.URLEncoder

class CreateGroupActivity : ComponentActivity() {
    lateinit var NameInput: EditText
    lateinit var loginBtn: TextView
    var id = ""
    var name = ""
    var selectgroup = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_group)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, SelectGroupActivity::class.java)
            startActivity(intent)
        }

        val file = File(this@CreateGroupActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    id = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@CreateGroupActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        selectgroup = intent.getStringExtra("GROUP").toString()

        NameInput = findViewById(R.id.name_input)

        loginBtn = findViewById(R.id.change_save)
        loginBtn.setOnClickListener {
            name = NameInput.text.toString()
            LoginTask().execute()
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {

            Log.d("CreateGroupActivity", "Rozpoczęto zadanie doInBackground")

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/group/create")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                val encodedName = URLEncoder.encode(name, "UTF-8").replace("+", "_")

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "name=$encodedName&search=$selectgroup&id_user=$id"
                Log.d("CreateGroupActivity", payload)
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                Log.d("CreateGroupActivity", connection.responseCode.toString())
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    println("Odpowiedź serwera: $response")

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
                val intent = Intent(this@CreateGroupActivity, YourGroupActivity::class.java)
                Toast.makeText(this@CreateGroupActivity, "Twoja grupa została utworzona.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this@CreateGroupActivity, "Napotkaliśmy problem! (yu73)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
