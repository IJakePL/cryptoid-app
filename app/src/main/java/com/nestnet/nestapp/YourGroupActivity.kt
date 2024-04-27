package com.nestnet.nestapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class YourGroupActivity : AppCompatActivity() {
    var names = ""
    var ids = mutableListOf<String>()
    var user_ids = ""
    lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_group)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val HomeButton: ImageButton = findViewById(R.id.home)

        HomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        var id_user = ""

        val file = File(this@YourGroupActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    names = jsonObject.getString("name")
                    id_user = jsonObject.getString("userId")
                    user_ids = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@YourGroupActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val textView1: TextView = findViewById(R.id.podana_nazwa)
        textView1.text = names

        LoginTask().execute(id_user)
    }

    private inner class LoginTask : AsyncTask<String, Void, JSONArray>() {
        override fun doInBackground(vararg params: String): JSONArray? {
            val id_user = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/group/search")
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
                    return JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: JSONArray?) {
            result?.let { jsonArray ->
                for (i in 0 until jsonArray.length()) {
                    val groupId = jsonArray.getString(i)
                    ids.add(groupId)
                    GroupInfoTask().execute(groupId)
                }
            }
        }
    }

    private inner class GroupInfoTask : AsyncTask<String, Void, JSONObject>() {
        override fun doInBackground(vararg params: String): JSONObject? {
            val groupId = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/group/search/info")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$groupId"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    return JSONObject(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: JSONObject?) {
            result?.let { groupInfo ->
                displayGroup(groupInfo)
            }
        }
    }

    private fun displayGroup(groupInfo: JSONObject) {
        try {
            val groupName = groupInfo.getString("name")
            val countMember = groupInfo.getString("count_member")
            val countGame = groupInfo.getString("count_game")
            val countOnline = groupInfo.getString("count_online")
            val groupSearch = groupInfo.getString("group_search")

            val newLinearLayout = LinearLayout(this@YourGroupActivity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                    bottomMargin = 10.dpToPx()
                }
                orientation = LinearLayout.HORIZONTAL
                background =
                    ContextCompat.getDrawable(this@YourGroupActivity, R.drawable.corner_ust)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2B2F33"))
                setPadding(15.dpToPx(), 15.dpToPx(), 15.dpToPx(), 15.dpToPx())
                setOnClickListener {
                    val index = indexOfChild(this)
                    if (index != -1 && index < ids.size) {
                        val groupId = ids[index]
                        val rejectTask = OpenGroup()
                        rejectTask.execute(groupId)
                    }
                }

                val imageButton1 = ImageButton(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        48.dpToPx(),
                        48.dpToPx()
                    )
                    background =
                        ContextCompat.getDrawable(this@YourGroupActivity, R.drawable.prof_2)
                    contentDescription = "przycisk logowanie"
                }

                val textView = TextView(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = 5.dpToPx()
                    }
                    text = "$groupName"
                    setTextColor(
                        ContextCompat.getColor(
                            this@YourGroupActivity,
                            R.color.colortextgroupname
                        )
                    )
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    setTypeface(null, Typeface.BOLD)
                }

                val horizontalLinearLayout = LinearLayout(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 6.dpToPx()
                    }
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }

                val horizontalLinearLayout1 = LinearLayout(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }

                val horizontalLinearLayout2 = LinearLayout(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = 10.dpToPx()
                    }
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_VERTICAL
                }

                if (groupSearch == "social") {
                    imageButton = ImageButton(this@YourGroupActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            24.dpToPx(),
                            24.dpToPx()
                        )
                        background =
                            ContextCompat.getDrawable(this@YourGroupActivity, R.drawable.social)
                        contentDescription = "przycisk logowanie"
                        backgroundTintList = ContextCompat.getColorStateList(this@YourGroupActivity, R.color.enabled)
                        backgroundTintMode = PorterDuff.Mode.SRC_IN
                    }
                } else {
                    imageButton = ImageButton(this@YourGroupActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            24.dpToPx(),
                            24.dpToPx()
                        )
                        background =
                            ContextCompat.getDrawable(this@YourGroupActivity, R.drawable.privates)
                        contentDescription = "przycisk logowanie"
                        backgroundTintList = ContextCompat.getColorStateList(this@YourGroupActivity, R.color.enabled)
                        backgroundTintMode = PorterDuff.Mode.SRC_IN
                    }
                }

                val greenCircle = ImageView(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(8.dpToPx(), 8.dpToPx()).apply {
                        gravity = Gravity.CENTER
                    }
                    setImageResource(R.drawable.corner_message)
                    setColorFilter(ContextCompat.getColor(this@YourGroupActivity, R.color.group_mem), PorterDuff.Mode.SRC_IN)
                }

                val grayCircle = ImageView(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(8.dpToPx(), 8.dpToPx()).apply {
                        gravity = Gravity.CENTER
                    }
                    setImageResource(R.drawable.corner_message)
                    setColorFilter(ContextCompat.getColor(this@YourGroupActivity, R.color.group_onl), PorterDuff.Mode.SRC_IN)
                }

                val memberTextView = TextView(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = 5.dpToPx()
                        gravity = Gravity.CENTER
                    }
                    text = "$countMember członków"
                    setTextColor(Color.parseColor("#8F949A"))
                    textSize = 14f
                }

                val onlineTextView = TextView(this@YourGroupActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = 5.dpToPx()
                        rightMargin = 10.dpToPx()
                        gravity = Gravity.CENTER
                    }
                    text = "$countOnline online"
                    setTextColor(Color.parseColor("#8F949A"))
                    textSize = 14f
                }

                horizontalLinearLayout.addView(greenCircle)
                horizontalLinearLayout.addView(onlineTextView)
                horizontalLinearLayout.addView(grayCircle)
                horizontalLinearLayout.addView(memberTextView)

                horizontalLinearLayout1.addView(imageButton)
                horizontalLinearLayout1.addView(textView)

                horizontalLinearLayout2.addView(horizontalLinearLayout1)
                horizontalLinearLayout2.addView(horizontalLinearLayout)

                addView(imageButton1)
                addView(horizontalLinearLayout2)
            }

            val linearLayout = findViewById<LinearLayout>(R.id.group)
            linearLayout.addView(newLinearLayout)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class OpenGroup : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String): Boolean {
            val id_group = params[0]
            val intent = Intent(this@YourGroupActivity, GroupShowActivity::class.java)

            intent.putExtra("groupid", id_group)

            runOnUiThread {
                startActivity(intent)
            }

            return true
        }
    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}
