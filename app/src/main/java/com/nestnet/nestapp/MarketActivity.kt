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

class MarketActivity : ComponentActivity() {

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
    var bchproc = ""
    var bnbproc = ""
    var cakeproc = ""
    var etcproc = ""
    var frxethproc = ""
    var gnoproc = ""
    var ltcproc = ""
    var methproc = ""
    var mkrproc = ""
    var neoproc = ""
    var ordiproc = ""
    var qntproc = ""
    var solproc = ""
    var swethproc = ""
    var xmrproc = ""

    var btc = ""
    var eth = ""
    var sushi = ""
    var doge = ""
    var bch = ""
    var bnb = ""
    var cake = ""
    var etc = ""
    var frxeth = ""
    var gno = ""
    var ltc = ""
    var meth = ""
    var mkr = ""
    var neo = ""
    var ordi = ""
    var qnt = ""
    var sol = ""
    var sweth = ""
    var xmr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.market)

        rewardedAd()
        curs().execute()

        val file = File(this@MarketActivity.filesDir, "user.json")

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
                val intent = Intent(this@MarketActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        if (wasConnected) {
            wasConnected = CheckNetwork.isWifiConnected(this@MarketActivity)
        }

        wasConnected = CheckNetwork.isWifiConnected(this@MarketActivity)

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        handler.postDelayed({
            adView.loadAd(adRequest)
        }, 30000)

        val MenuButton: EditText = findViewById(R.id.search)
        val Action: ImageButton = findViewById(R.id.action)
        val Chat: ImageButton = findViewById(R.id.chat)
        val home: ImageButton = findViewById(R.id.home)

        val calendar = Calendar.getInstance()
        val locale = Locale("pl", "PL")
        val dateFormat = SimpleDateFormat("d MMM yyyy", locale)
        val formattedDate = dateFormat.format(calendar.time)

        Chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MarketActivity).toBundle()
            startActivity(intent, bundel)
        }

        home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MarketActivity).toBundle()
            startActivity(intent, bundel)
        }

        Action.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            val bundel = ActivityOptions.makeSceneTransitionAnimation(this@MarketActivity).toBundle()
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
                    bchproc = jsonResponse.getString("bchproc")
                    bnbproc = jsonResponse.getString("bnbproc")
                    cakeproc = jsonResponse.getString("cakeproc")
                    etcproc = jsonResponse.getString("etcproc")
                    frxethproc = jsonResponse.getString("frxethproc")
                    gnoproc = jsonResponse.getString("gnoproc")
                    ltcproc = jsonResponse.getString("ltcproc")
                    methproc = jsonResponse.getString("methproc")
                    mkrproc = jsonResponse.getString("mkrproc")
                    neoproc = jsonResponse.getString("neoproc")
                    ordiproc = jsonResponse.getString("ordiproc")
                    qntproc = jsonResponse.getString("qntproc")
                    solproc = jsonResponse.getString("solproc")
                    swethproc = jsonResponse.getString("swethproc")
                    xmrproc = jsonResponse.getString("xmrproc")

                    btc = jsonResponse.getString("btc")
                    eth = jsonResponse.getString("eth")
                    sushi = jsonResponse.getString("sushi")
                    doge = jsonResponse.getString("doge")
                    bch = jsonResponse.getString("bch")
                    bnb = jsonResponse.getString("bnb")
                    cake = jsonResponse.getString("cake")
                    etc = jsonResponse.getString("etc")
                    frxeth = jsonResponse.getString("frxeth")
                    gno = jsonResponse.getString("gno")
                    ltc = jsonResponse.getString("ltc")
                    meth = jsonResponse.getString("meth")
                    mkr = jsonResponse.getString("mkr")
                    neo = jsonResponse.getString("neo")
                    ordi = jsonResponse.getString("ordi")
                    qnt = jsonResponse.getString("qnt")
                    sol = jsonResponse.getString("sol")
                    sweth = jsonResponse.getString("sweth")
                    xmr = jsonResponse.getString("xmr")

                    runOnUiThread {
                        if (btcproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upbtc)
                            val downbtc: LinearLayout = findViewById(R.id.downbtc)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upbtcvalue)
                            upbtcvalue.text = "+" + btcproc + "%"
                            val btcprice: TextView = findViewById(R.id.btcprice)
                            btcprice.text = btc
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downbtc)
                            val upbtc: LinearLayout = findViewById(R.id.upbtc)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downbtcvalue)
                            downbtcvalue.text = btcproc + "%"
                            val btcprice: TextView = findViewById(R.id.btcprice)
                            btcprice.text = btc
                        }

                        if (ethproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upethereum)
                            val downbtc: LinearLayout = findViewById(R.id.downethereum)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upethereumvalue)
                            upbtcvalue.text = "+" + ethproc + "%"
                            val ethprice: TextView = findViewById(R.id.ethprice)
                            ethprice.text = eth
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downethereum)
                            val upbtc: LinearLayout = findViewById(R.id.upethereum)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downethereumvalue)
                            downbtcvalue.text = ethproc + "%"
                            val btcprice: TextView = findViewById(R.id.ethprice)
                            btcprice.text = eth
                        }

                        if (sushiproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upsushi)
                            val downbtc: LinearLayout = findViewById(R.id.downsushi)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upsushivalue)
                            upbtcvalue.text = "+" + sushiproc + "%"
                            val btcprice: TextView = findViewById(R.id.sushiprice)
                            btcprice.text = sushi
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downsushi)
                            val upbtc: LinearLayout = findViewById(R.id.upsushi)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downsushivalue)
                            downbtcvalue.text = sushiproc + "%"
                            val btcprice: TextView = findViewById(R.id.sushiprice)
                            btcprice.text = sushi
                        }

                        if (dogeproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.updoge)
                            val downbtc: LinearLayout = findViewById(R.id.downdoge)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.updogevalue)
                            upbtcvalue.text = "+" + dogeproc + "%"
                            val btcprice: TextView = findViewById(R.id.dogeprice)
                            btcprice.text = doge
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downdoge)
                            val upbtc: LinearLayout = findViewById(R.id.updoge)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downdogevalue)
                            downbtcvalue.text = dogeproc + "%"
                            val btcprice: TextView = findViewById(R.id.dogeprice)
                            btcprice.text = doge
                        }

                        if (bchproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upbch)
                            val downbtc: LinearLayout = findViewById(R.id.downbch)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upbchvalue)
                            upbtcvalue.text = "+" + bchproc + "%"
                            val btcprice: TextView = findViewById(R.id.bchprice)
                            btcprice.text = bch
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downbch)
                            val upbtc: LinearLayout = findViewById(R.id.upbch)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downbchvalue)
                            downbtcvalue.text = bchproc + "%"
                            val btcprice: TextView = findViewById(R.id.bchprice)
                            btcprice.text = bch
                        }

                        if (bnbproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upbnb)
                            val downbtc: LinearLayout = findViewById(R.id.downbnb)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upbnbvalue)
                            upbtcvalue.text = "+" + bnbproc + "%"
                            val btcprice: TextView = findViewById(R.id.bnbprice)
                            btcprice.text = bnb
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downbnb)
                            val upbtc: LinearLayout = findViewById(R.id.upbnb)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downbnbvalue)
                            downbtcvalue.text = bnbproc + "%"
                            val btcprice: TextView = findViewById(R.id.bnbprice)
                            btcprice.text = bnb
                        }

                        if (cakeproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upcake)
                            val downbtc: LinearLayout = findViewById(R.id.downcake)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upcakevalue)
                            upbtcvalue.text = "+" + cakeproc + "%"
                            val btcprice: TextView = findViewById(R.id.cakeprice)
                            btcprice.text = cake
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downcake)
                            val upbtc: LinearLayout = findViewById(R.id.upcake)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downcakevalue)
                            downbtcvalue.text = cakeproc + "%"
                            val btcprice: TextView = findViewById(R.id.cakeprice)
                            btcprice.text = cake
                        }

                        if (etcproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upetc)
                            val downbtc: LinearLayout = findViewById(R.id.downetc)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upetcvalue)
                            upbtcvalue.text = "+" + etcproc + "%"
                            val btcprice: TextView = findViewById(R.id.etcprice)
                            btcprice.text = etc
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downetc)
                            val upbtc: LinearLayout = findViewById(R.id.upetc)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downetcvalue)
                            downbtcvalue.text = etcproc + "%"
                            val btcprice: TextView = findViewById(R.id.etcprice)
                            btcprice.text = etc
                        }

                        if (frxethproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upfrxeth)
                            val downbtc: LinearLayout = findViewById(R.id.downfrxeth)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upfrxethvalue)
                            upbtcvalue.text = "+" + frxethproc + "%"
                            val btcprice: TextView = findViewById(R.id.frxethprice)
                            btcprice.text = frxeth
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downfrxeth)
                            val upbtc: LinearLayout = findViewById(R.id.upfrxeth)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downfrxethvalue)
                            downbtcvalue.text = frxethproc + "%"
                            val btcprice: TextView = findViewById(R.id.frxethprice)
                            btcprice.text = frxeth
                        }

                        if (gnoproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upgno)
                            val downbtc: LinearLayout = findViewById(R.id.downgno)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upgnovalue)
                            upbtcvalue.text = "+" + gnoproc + "%"
                            val btcprice: TextView = findViewById(R.id.gnoprice)
                            btcprice.text = doge
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downgno)
                            val upbtc: LinearLayout = findViewById(R.id.upgno)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downgnovalue)
                            downbtcvalue.text = gnoproc + "%"
                            val btcprice: TextView = findViewById(R.id.gnoprice)
                            btcprice.text = gno
                        }

                        if (ltcproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upltc)
                            val downbtc: LinearLayout = findViewById(R.id.downltc)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upltcvalue)
                            upbtcvalue.text = "+" + ltcproc + "%"
                            val btcprice: TextView = findViewById(R.id.ltcprice)
                            btcprice.text = ltc
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downltc)
                            val upbtc: LinearLayout = findViewById(R.id.upltc)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downltcvalue)
                            downbtcvalue.text = ltcproc + "%"
                            val btcprice: TextView = findViewById(R.id.ltcprice)
                            btcprice.text = ltc
                        }

                        if (methproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upmeth)
                            val downbtc: LinearLayout = findViewById(R.id.downmeth)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upmethvalue)
                            upbtcvalue.text = "+" + methproc + "%"
                            val btcprice: TextView = findViewById(R.id.methprice)
                            btcprice.text = meth
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downmeth)
                            val upbtc: LinearLayout = findViewById(R.id.upmeth)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downmethvalue)
                            downbtcvalue.text = methproc + "%"
                            val btcprice: TextView = findViewById(R.id.methprice)
                            btcprice.text = meth
                        }

                        if (mkrproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upmkr)
                            val downbtc: LinearLayout = findViewById(R.id.downmkr)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upmkrvalue)
                            upbtcvalue.text = "+" + dogeproc + "%"
                            val btcprice: TextView = findViewById(R.id.mkrprice)
                            btcprice.text = mkr
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downmkr)
                            val upbtc: LinearLayout = findViewById(R.id.upmkr)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downmkrvalue)
                            downbtcvalue.text = mkrproc + "%"
                            val btcprice: TextView = findViewById(R.id.mkrprice)
                            btcprice.text = mkr
                        }

                        if (neoproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upneo)
                            val downbtc: LinearLayout = findViewById(R.id.downneo)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upneovalue)
                            upbtcvalue.text = "+" + neoproc + "%"
                            val btcprice: TextView = findViewById(R.id.neoprice)
                            btcprice.text = neo
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downneo)
                            val upbtc: LinearLayout = findViewById(R.id.upneo)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downneovalue)
                            downbtcvalue.text = neoproc + "%"
                            val btcprice: TextView = findViewById(R.id.neoprice)
                            btcprice.text = neo
                        }

                        if (ordiproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upordi)
                            val downbtc: LinearLayout = findViewById(R.id.downordi)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upordivalue)
                            upbtcvalue.text = "+" + ordiproc + "%"
                            val btcprice: TextView = findViewById(R.id.ordiprice)
                            btcprice.text = ordi
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downordi)
                            val upbtc: LinearLayout = findViewById(R.id.upordi)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downordivalue)
                            downbtcvalue.text = ordiproc + "%"
                            val btcprice: TextView = findViewById(R.id.ordiprice)
                            btcprice.text = ordi
                        }

                        if (qntproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upqnt)
                            val downbtc: LinearLayout = findViewById(R.id.downqnt)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upqntvalue)
                            upbtcvalue.text = "+" + qntproc + "%"
                            val btcprice: TextView = findViewById(R.id.qntprice)
                            btcprice.text = qnt
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downqnt)
                            val upbtc: LinearLayout = findViewById(R.id.upqnt)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downqntvalue)
                            downbtcvalue.text = qntproc + "%"
                            val btcprice: TextView = findViewById(R.id.qntprice)
                            btcprice.text = qnt
                        }

                        if (solproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upsol)
                            val downbtc: LinearLayout = findViewById(R.id.downsol)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upsolvalue)
                            upbtcvalue.text = "+" + solproc + "%"
                            val btcprice: TextView = findViewById(R.id.solprice)
                            btcprice.text = sol
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downsol)
                            val upbtc: LinearLayout = findViewById(R.id.upsol)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downsolvalue)
                            downbtcvalue.text = solproc + "%"
                            val btcprice: TextView = findViewById(R.id.solprice)
                            btcprice.text = sol
                        }

                        if (swethproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upsweth)
                            val downbtc: LinearLayout = findViewById(R.id.downsweth)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upswethvalue)
                            upbtcvalue.text = "+" + swethproc + "%"
                            val btcprice: TextView = findViewById(R.id.swethprice)
                            btcprice.text = sweth
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downsweth)
                            val upbtc: LinearLayout = findViewById(R.id.upsweth)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downswethvalue)
                            downbtcvalue.text = swethproc + "%"
                            val btcprice: TextView = findViewById(R.id.swethprice)
                            btcprice.text = sweth
                        }

                        if (xmrproc > "0") {
                            val upbtc: LinearLayout = findViewById(R.id.upxmr)
                            val downbtc: LinearLayout = findViewById(R.id.downxmr)
                            upbtc.visibility = View.VISIBLE
                            downbtc.visibility = View.GONE
                            val upbtcvalue: TextView = findViewById(R.id.upxmrvalue)
                            upbtcvalue.text = "+" + xmrproc + "%"
                            val btcprice: TextView = findViewById(R.id.xmrprice)
                            btcprice.text = xmr
                        } else {
                            val downbtc: LinearLayout = findViewById(R.id.downxmr)
                            val upbtc: LinearLayout = findViewById(R.id.upxmr)
                            downbtc.visibility = View.VISIBLE
                            upbtc.visibility = View.GONE
                            val downbtcvalue: TextView = findViewById(R.id.downxmrvalue)
                            downbtcvalue.text = xmrproc + "%"
                            val btcprice: TextView = findViewById(R.id.xmrprice)
                            btcprice.text = xmr
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