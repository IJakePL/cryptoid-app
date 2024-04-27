package com.nestnet.nestapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.os.AsyncTask
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ConnectAccountActivity : ComponentActivity() {

    lateinit var NameInput: EditText
    lateinit var loginBtn: TextView

    var name = ""
    var type = ""
    var urls = ""
    var userid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_account)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, KontoActivity::class.java)
            startActivity(intent)
        }

        val MethodButton: TextView = findViewById(R.id.dodaj)

        MethodButton.setOnClickListener {
            val intent = Intent(this, ConnectAccountMethodActivity::class.java)
            startActivity(intent)
        }

        val file = File(this@ConnectAccountActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    userid = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ConnectAccountActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        SendTack().execute(userid)
    }

    private inner class SendTack : AsyncTask<String, Void, List<String>?>() {
        var id_user = ""
        override fun doInBackground(vararg params: String): List<String>? {
            id_user = params[0]

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/account_connect")
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

                    val namesList = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        namesList.add(name)
                    }

                    return namesList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)
            result?.let { names ->
                for (name in names) {
                    val newLinearLayout = LinearLayout(this@ConnectAccountActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        ).apply {
                            gravity = Gravity.CENTER
                            topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
                        }
                        orientation = LinearLayout.VERTICAL
                        background = ContextCompat.getDrawable(
                            this@ConnectAccountActivity,
                            R.drawable.corner_ust
                        )
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
                                setImageResource(R.drawable.discord)
                                setColorFilter(
                                    Color.parseColor("#5865F2"),
                                    PorterDuff.Mode.SRC_IN
                                )
                            }

                            val textView1 = TextView(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    gravity = Gravity.CENTER
                                }
                                text = names.firstOrNull()
                                setTextColor(Color.parseColor("#DFE3E6"))
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

                    val linearLayout = findViewById<LinearLayout>(R.id.all_connect)
                    linearLayout.addView(newLinearLayout)
                }
            }
        }
    }
}
