package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.nestnet.nestapp.models.ChatMessage
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import com.nestnet.nestapp.utils.CheckNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.FileReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeActivity : ComponentActivity() {

    private var wasConnected = false
    private final var TAG = "MainActivity"
    private var rewardedAd: RewardedAd? = null
    private val handler = Handler(Looper.getMainLooper())

    var name = ""
    var plan = ""
    var userId = ""
    var emails = ""

    var btcproc = ""
    var ethproc = ""
    var sushiproc = ""
    var dogeproc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        rewardedAd()
        curs().execute()

        val file = File(this@HomeActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    emails = jsonObject.getString("email")
                    name = jsonObject.getString("name")
                    plan = jsonObject.getString("plan")
                    userId = jsonObject.getString("userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        if (wasConnected) {
            wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)
        }

        wasConnected = CheckNetwork.isWifiConnected(this@HomeActivity)

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        handler.postDelayed({
            adView.loadAd(adRequest)
        }, 30000)

        val MenuButton: EditText = findViewById(R.id.search)
        val Profile: LinearLayout = findViewById(R.id.profile)
        val Action: ImageButton = findViewById(R.id.action)
        val Chat: ImageButton = findViewById(R.id.chat)
        val market: ImageButton = findViewById(R.id.market)

        val calendar = Calendar.getInstance()
        val locale = Locale("pl", "PL")
        val dateFormat = SimpleDateFormat("d MMM yyyy", locale)
        val formattedDate = dateFormat.format(calendar.time)

        Chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@HomeActivity).toBundle()
            startActivity(intent, bundel)
        }

        Profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@HomeActivity).toBundle()
            startActivity(intent, bundel)
        }

        market.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@HomeActivity).toBundle()
            startActivity(intent, bundel)
        }

        Action.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@HomeActivity).toBundle()
            startActivity(intent, bundel)
        }

//        reward.setOnClickListener {
//            rewardedAd?.let { ad ->
//                ad.show(this, OnUserEarnedRewardListener { rewardItem ->
//                    Log.d(TAG, "User earned the reward.")
//                })
//            } ?: run {
//                Log.d(TAG, "The rewarded ad wasn't ready yet.")
//            }
//        }

        val welc_name: TextView = findViewById(R.id.welc_name)
        welc_name.setText("$name")
    }

    private fun rewardedAd() {
        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,"ca-app-pub-6928750575920542/7862692810", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                rewardedAd = ad
            }
        })
    }

    override fun onResume() {
        super.onResume()
        OnlineActv().execute(userId)
    }

    override fun onPause() {
        super.onPause()
        IdleActv().execute(userId)
    }

    override fun onStop() {
        super.onStop()
        OfflineActv().execute(userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        OfflineActv().execute(userId)
    }

    private inner class curs : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/v1/api-pro/simple/price")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonResponse = JSONObject(response)

                    btcproc = jsonResponse.getString("btcproc")
                    ethproc = jsonResponse.getString("ethproc")
                    sushiproc = jsonResponse.getString("sushiproc")
                    dogeproc = jsonResponse.getString("dogeproc")

                    runOnUiThread {
                        if (btcproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upbtc)
                            val downbtc: LinearLayout = findViewById(R.id.downbtc)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upbtcvalue)
                            upbtcvalue.text = "+" + btcproc + "%"
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downbtc)
                            val upbtc: LinearLayout = findViewById(R.id.upbtc)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downbtcvalue)
                            downbtcvalue.text = btcproc + "%"
                        }

                        if (ethproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upethereum)
                            val downbtc: LinearLayout = findViewById(R.id.downethereum)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upethereumvalue)
                            upbtcvalue.text = "+" + ethproc + "%"
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downethereum)
                            val upbtc: LinearLayout = findViewById(R.id.upethereum)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downethereumvalue)
                            downbtcvalue.text = ethproc + "%"
                        }

                        if (sushiproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upsushi)
                            val downbtc: LinearLayout = findViewById(R.id.downsushi)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upsushivalue)
                            upbtcvalue.text = "+" + sushiproc + "%"
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downsushi)
                            val upbtc: LinearLayout = findViewById(R.id.upsushi)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downsushivalue)
                            downbtcvalue.text = sushiproc + "%"
                        }

                        if (dogeproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.updoge)
                            val downbtc: LinearLayout = findViewById(R.id.downdoge)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.updogevalue)
                            upbtcvalue.text = "+" + dogeproc + "%"
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downdoge)
                            val upbtc: LinearLayout = findViewById(R.id.updoge)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downdogevalue)
                            downbtcvalue.text = dogeproc + "%"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }

        override fun onPostExecute(result: List<String>) {
            handler.postDelayed({
                curs().execute()
            }, 30000)
        }
    }

    private inner class HashCreate : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val calendar = Calendar.getInstance()
                val date = SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.time)

                val url = URL("http://fi3.bot-hosting.net:20688/api/hash/rate/boost")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "userId=$id_user&startTime=$date"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }
    }

//    private inner class HashSearch : AsyncTask<String, Void, List<String>>() {
//        override fun doInBackground(vararg params: String): List<String> {
//            val id_user = params[0]
//            val results = mutableListOf<String>()
//
//            try {
//                val url = URL("http://fi3.bot-hosting.net:20688/api/hash/rate/boost/search")
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "POST"
//                connection.doOutput = true
//
//                val outputStream = OutputStreamWriter(connection.outputStream)
//                val payload = "userId=$id_user"
//                outputStream.write(payload)
//                outputStream.flush()
//
//                val responseCode = connection.responseCode
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    val inputStream = connection.inputStream
//                    val response = inputStream.bufferedReader().use { it.readText() }
//
//                    val jsonResponse = JSONObject(response)
//                    time = jsonResponse.getString("time")
//
//                    runOnUiThread {
//                        val booster: LinearLayout = findViewById(R.id.booster)
//                        booster.isEnabled = false
//                        val timeend: TextView = findViewById(R.id.timeend)
//                        timeend.text = time
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            return results
//        }
//
//        override fun onPostExecute(result: List<String>) {
//            handler.postDelayed({
//                HashSearch().execute(userId)
//            }, 2000)
//        }
//    }

    private inner class OnlineActv : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/status/user/change")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user&status=Online"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }
    }

    private inner class IdleActv : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/status/user/change")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user&status=Idle"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }
    }

    private inner class OfflineActv : AsyncTask<String, Void, List<String>>() {
        override fun doInBackground(vararg params: String): List<String> {
            val id_user = params[0]
            val results = mutableListOf<String>()

            try {
                val url = URL("http://fi3.bot-hosting.net:20688/api/search/status/user/change")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val outputStream = OutputStreamWriter(connection.outputStream)
                val payload = "id_user=$id_user&status=Offline"
                outputStream.write(payload)
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return results
        }
    }
}