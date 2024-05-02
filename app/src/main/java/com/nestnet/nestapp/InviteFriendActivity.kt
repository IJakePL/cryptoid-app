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

class InviteFriendActivity : ComponentActivity() {

    lateinit var NameInput: EditText
    lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invite_friend)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@InviteFriendActivity).toBundle()
            startActivity(intent, bundel)
        }

        var id_sender = ""

        val file = File(this@InviteFriendActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    id_sender = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@InviteFriendActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@InviteFriendActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.id_usera)
        textView1.setText(id_sender)

        NameInput = findViewById(R.id.id_input)
        loginBtn = findViewById(R.id.change_save)

        loginBtn.setOnClickListener {
            val Names = NameInput.text.toString()

            if (Names.isNotEmpty()) {
                LoginTask().execute(Names, id_sender)
            } else {
                if (Names.isEmpty()) {
                    NameInput.error = "Musisz wypełnić te pole!"
                }

                Toast.makeText(this, "Musisz podać id użytkownika!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_user = params[0]
            val id_sender = params[1]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/invite/friend/wait")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user&id_sender=$id_sender"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
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
                val intent = Intent(this@InviteFriendActivity, InviteFriendActivity::class.java)
                Toast.makeText(this@InviteFriendActivity, "Zaproszenie zostało wysłane", Toast.LENGTH_SHORT).show()
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@InviteFriendActivity).toBundle()
                startActivity(intent, bundel)
            } else {
                Toast.makeText(this@InviteFriendActivity, "Napotkaliśmy problem! (ad98)", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
