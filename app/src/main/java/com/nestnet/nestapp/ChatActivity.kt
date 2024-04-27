package com.nestnet.nestapp

import Task
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nestnet.nestapp.utils.ChatAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ChatActivity : AppCompatActivity() {
    var name = ""
    var ids = ""
    var user_ids = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val InviteButton: ImageButton = findViewById(R.id.invite_friends)

        InviteButton.setOnClickListener {
            val intent = Intent(this, InviteFriendActivity::class.java)
            startActivity(intent)
        }

        val menu: ImageButton = findViewById(R.id.menu)

        menu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val allFriendsLayout = findViewById<LinearLayout>(R.id.all_friends)
        val wyslaneLayout = findViewById<LinearLayout>(R.id.wyslane)
        val oczekujaceLayout = findViewById<LinearLayout>(R.id.oczekujace)

        // allfriends

        val pending: TextView = findViewById(R.id.pending)
        val waiting: TextView = findViewById(R.id.waiting)

        pending.setOnClickListener {
            allFriendsLayout.visibility = View.GONE
            wyslaneLayout.visibility = View.VISIBLE
        }

        waiting.setOnClickListener {
            allFriendsLayout.visibility = View.GONE
            oczekujaceLayout.visibility = View.VISIBLE
        }

        // pending

        val friends: TextView = findViewById(R.id.friends)
        val waiting1: TextView = findViewById(R.id.waiting1)

        friends.setOnClickListener {
            wyslaneLayout.visibility = View.GONE
            allFriendsLayout.visibility = View.VISIBLE
        }

        waiting1.setOnClickListener {
            wyslaneLayout.visibility = View.GONE
            oczekujaceLayout.visibility = View.VISIBLE
        }

        // waiting

        val friends1: TextView = findViewById(R.id.friends1)
        val pending1: TextView = findViewById(R.id.pending1)

        friends1.setOnClickListener {
            oczekujaceLayout.visibility = View.GONE
            allFriendsLayout.visibility = View.VISIBLE
        }

        pending1.setOnClickListener {
            oczekujaceLayout.visibility = View.GONE
            wyslaneLayout.visibility = View.VISIBLE
        }

        var id_user = ""

        val file = File(this@ChatActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    id_user = jsonObject.getString("userId")
                    user_ids = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ChatActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        AcceptInv().execute(id_user)
        LoginTask().execute(id_user)
        SendTask().execute(id_user)

    }

    private inner class SendAcceptTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_user = params[0]
            val id_sender = user_ids

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/invite/friend/accept")
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
                val intent = Intent(this@ChatActivity, ChatActivity::class.java)
                Toast.makeText(this@ChatActivity, "Zaproszenie zostało zaakceptowane", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@ChatActivity, "Napotkaliśmy problem! (ry26)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class AcceptInv : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/accept")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val value = jsonArray.getString(i)
                        results.add(value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }

        var user_ids = ""

        override fun onPostExecute(result: List<String>) {
            for (value in result) {
                user_ids = value
                AccIdTask().execute(user_ids)
            }
        }
    }

    private inner class SendRejectTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_user = params[0]
            val id_sender = user_ids

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/invite/friend/send/cancel")
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
                val intent = Intent(this@ChatActivity, ChatActivity::class.java)
                Toast.makeText(this@ChatActivity, "Zaproszenie zostało cofnięte", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@ChatActivity, "Napotkaliśmy problem! (ry26)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class RejectTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_user = params[0]
            val id_sender = user_ids

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/invite/friend/cancel")
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
                val intent = Intent(this@ChatActivity, ChatActivity::class.java)
                Toast.makeText(this@ChatActivity, "Zaproszenie zostało odrzucone", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@ChatActivity, "Napotkaliśmy problem! (ry26)", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private inner class LoginTask : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/wait")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val value = jsonArray.getString(i)
                        results.add(value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }

        var user_ids = ""

        override fun onPostExecute(result: List<String>) {
            for (value in result) {
                user_ids = value
                IdTask().execute(user_ids)
            }
        }
    }

    private inner class SendTask : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/send")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val value = jsonArray.getString(i)
                        results.add(value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }

        var user_ids = ""

        override fun onPostExecute(result: List<String>) {
            for (value in result) {
                user_ids = value
                IdSendTask().execute(user_ids)
            }
        }
    }

    private inner class IdTask : AsyncTask<String, Void, List<String>?>() {
        var id_user = ""
        override fun doInBackground(vararg params: String): List<String>? {
            id_user = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/info")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    ids = jsonResponse.getString("id")
                    name = jsonResponse.getString("name")

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return listOf(name ?: "")
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)
            result?.let { name ->
                val newLinearLayout = LinearLayout(this@ChatActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = Gravity.CENTER
                        topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
                    }
                    orientation = LinearLayout.VERTICAL
                    background = ContextCompat.getDrawable(this@ChatActivity, R.drawable.corner_ust)
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2B2F33"))

                    val innerLinearLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        orientation = LinearLayout.HORIZONTAL

                        val imageButton = ImageButton(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.image_button_width),
                                resources.getDimensionPixelSize(R.dimen.image_button_height)
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            background = null
                            contentDescription = "przycisk menu"
                            setPadding(
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding)
                            )
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.envelope)
                            setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)
                        }

                        val textView1 = TextView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            text = name.firstOrNull()
                            setTextColor(Color.parseColor("#878787"))
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                            setTypeface(null, Typeface.BOLD)
                            visibility = View.VISIBLE
                        }

                        val spacer = View(context).apply {
                            layoutParams = LinearLayout.LayoutParams(0, 0).apply {
                                weight = 1f
                            }
                        }

                        val accept = ImageButton(context).apply {
                            val buttonId = ids.toIntOrNull() ?: View.generateViewId()
                            id = buttonId
                            val marginRight = resources.getDimensionPixelSize(R.dimen.margin_right)
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.button_size),
                                resources.getDimensionPixelSize(R.dimen.button_size)
                            ).apply {
                                gravity = Gravity.CENTER
                                rightMargin = marginRight
                            }
                            setBackgroundResource(R.drawable.corner_prf)
                            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#212529"))
                            contentDescription = "przycisk menu"
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.check)
                            setColorFilter(Color.parseColor("#95CE41"), PorterDuff.Mode.SRC_IN)
                            setOnClickListener {
                                val rejectTask = SendAcceptTask()
                                rejectTask.execute(id_user)
                            }
                        }

                        val remove = ImageButton(context).apply {
                            val marginRight = resources.getDimensionPixelSize(R.dimen.margin_right)
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.button_size),
                                resources.getDimensionPixelSize(R.dimen.button_size)
                            ).apply {
                                gravity = Gravity.CENTER
                                rightMargin = marginRight
                            }
                            setBackgroundResource(R.drawable.corner_prf)
                            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#212529"))
                            contentDescription = "przycisk menu"
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.cancel)
                            setColorFilter(Color.parseColor("#CE4141"), PorterDuff.Mode.SRC_IN)
                            setOnClickListener {
                                val rejectTask = RejectTask()
                                rejectTask.execute(id_user)
                            }
                        }

                        addView(imageButton)
                        addView(textView1)
                        addView(spacer)
                        addView(accept)
                        addView(remove)
                    }

                    addView(innerLinearLayout)
                }

                val linearLayout = findViewById<LinearLayout>(R.id.oczekujace)
                linearLayout.addView(newLinearLayout)
            }
        }
    }

    private inner class IdSendTask : AsyncTask<String, Void, List<String>?>() {
        var id_user = ""
        override fun doInBackground(vararg params: String): List<String>? {
            id_user = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/info/send")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    name = jsonResponse.getString("name")
                    ids = jsonResponse.getString("id")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return listOf(name ?: "")
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)
            result?.let { name ->
                val newLinearLayout = LinearLayout(this@ChatActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = Gravity.CENTER
                        topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
                    }
                    orientation = LinearLayout.VERTICAL
                    background = ContextCompat.getDrawable(this@ChatActivity, R.drawable.corner_ust)
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2B2F33"))

                    val innerLinearLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        orientation = LinearLayout.HORIZONTAL

                        val imageButton = ImageButton(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.image_button_width),
                                resources.getDimensionPixelSize(R.dimen.image_button_height)
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            background = null
                            contentDescription = "przycisk menu"
                            setPadding(
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding),
                                resources.getDimensionPixelSize(R.dimen.image_button_padding)
                            )
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.envelope)
                            setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)
                        }

                        val textView1 = TextView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            text = name.firstOrNull()
                            setTextColor(Color.parseColor("#878787"))
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                            setTypeface(null, Typeface.BOLD)
                            visibility = View.VISIBLE
                        }

                        val spacer = View(context).apply {
                            layoutParams = LinearLayout.LayoutParams(0, 0).apply {
                                weight = 1f
                            }
                        }

                        val remove = ImageButton(context).apply {
                            val buttonId = ids.toIntOrNull() ?: View.generateViewId()
                            id = buttonId
                            val marginRight = resources.getDimensionPixelSize(R.dimen.margin_right)
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.button_size),
                                resources.getDimensionPixelSize(R.dimen.button_size)
                            ).apply {
                                gravity = Gravity.CENTER
                                rightMargin = marginRight
                            }
                            setBackgroundResource(R.drawable.corner_prf)
                            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#212529"))
                            contentDescription = "przycisk menu"
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.cancel)
                            setColorFilter(Color.parseColor("#CE4141"), PorterDuff.Mode.SRC_IN)
                            setOnClickListener {
                                val rejectTask = SendRejectTask()
                                rejectTask.execute(id_user)
                            }
                        }

                        addView(imageButton)
                        addView(textView1)
                        addView(spacer)
                        addView(remove)
                    }

                    addView(innerLinearLayout)
                }

                val linearLayout = findViewById<LinearLayout>(R.id.wyslane)
                linearLayout.addView(newLinearLayout)
            }
        }
    }

    private inner class AccIdTask : AsyncTask<String, Void, List<String>?>() {
        var id_user = ""
        override fun doInBackground(vararg params: String): List<String>? {
            id_user = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/friend/info/accept")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    ids = jsonResponse.getString("id")
                    name = jsonResponse.getString("name")

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return listOf(name ?: "")
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)
            result?.let { name ->
                val newLinearLayout = LinearLayout(this@ChatActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = Gravity.CENTER
                        topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
                    }
                    orientation = LinearLayout.VERTICAL
                    background = ContextCompat.getDrawable(this@ChatActivity,
                        R.drawable.corner_chat_layer
                    )
                    setOnClickListener {
                        val openchat = OpenChat()
                        openchat.execute(id_user, name.firstOrNull())
                    }

                    val innerLinearLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        orientation = LinearLayout.HORIZONTAL

                        val imageButton = ImageButton(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                resources.getDimensionPixelSize(R.dimen.image_button_width),
                                resources.getDimensionPixelSize(R.dimen.image_button_height)
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            background = null
                            contentDescription = "przycisk menu"
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            setImageResource(R.drawable.img_7)
                        }

                        val textView1 = TextView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            text = name.firstOrNull()
                            setTextColor(Color.parseColor("#79C8EF"))
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                            setTypeface(null, Typeface.BOLD)
                            visibility = View.VISIBLE
                        }

                        val spacer = View(context).apply {
                            layoutParams = LinearLayout.LayoutParams(0, 0).apply {
                                weight = 1f
                            }
                        }


                        addView(imageButton)
                        addView(textView1)
                        addView(spacer)
                    }

                    addView(innerLinearLayout)
                }

                val linearLayout = findViewById<LinearLayout>(R.id.all_friends)
                linearLayout.addView(newLinearLayout)
            }
        }
    }

    private inner class OpenChat : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_user = params[0]
            val name = params[1]
            val intent = Intent(this@ChatActivity, ChatMessageActivity::class.java)

            intent.putExtra("userid", id_user)
            intent.putExtra("name", name)

            runOnUiThread {
                startActivity(intent)
            }

            return true
        }
    }

}