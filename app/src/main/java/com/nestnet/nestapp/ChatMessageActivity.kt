package com.nestnet.nestapp

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestnet.nestapp.models.ChatMessage
import com.nestnet.nestapp.utils.ChatAdapter
import com.nestnet.nestapp.utils.ChatApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileReader
import java.util.Calendar
import java.text.SimpleDateFormat

class ChatMessageActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private var currentUser: String = ""
    private lateinit var chatApiService: ChatApiService.Service
    var name = ""
    var ids = ""
    var currentName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_message)

        fetchMessagesPeriodically()

        val received = intent.getStringExtra("userid") ?: ""
        val receivedName = intent.getStringExtra("name") ?: ""

        val file = File(this@ChatMessageActivity.filesDir, "user.json")

        if (file.exists()) {
            try {
                val fileReader = FileReader(file)
                val jsonString = fileReader.readText()

                if (jsonString.isNotEmpty()) {
                    val jsonObject = JSONObject(jsonString)
                    currentUser = jsonObject.getString("userId")
                    currentName = jsonObject.getString("name")

                    val intent = Intent(this@ChatMessageActivity, ChatAdapter::class.java)
                    intent.putExtra("userid", currentUser)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(this@ChatMessageActivity, MainActivity::class.java)
                val bundel = ActivityOptions.makeSceneTransitionAnimation(this@ChatMessageActivity).toBundle()
                startActivity(intent, bundel)
                finish()
            }
        }


        val recyclerViewMessages: RecyclerView = findViewById(R.id.recyclerViewMessages)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://fi3.bot-hosting.net:20688")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        chatApiService = retrofit.create(ChatApiService.Service::class.java)

        chatAdapter = ChatAdapter(currentUser, currentName, name)
        chatAdapter.setReceiverName(receivedName)
        recyclerViewMessages.adapter = chatAdapter

        val textView1: TextView = findViewById(R.id.nickname)
        textView1.setText(receivedName)

        val textView2: EditText = findViewById(R.id.editTextMessage)
        textView2.setHint("Napisz do " + receivedName)

        val MenuButton: ImageButton = findViewById(R.id.home_button1)

        MenuButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        recyclerViewMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatMessageActivity)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val allMessagesResponse = chatApiService.getAllMessages(currentUser, received)
            val allMessages = allMessagesResponse.map { chatMessageResponse ->
                ChatMessage(
                    sender = chatMessageResponse.sender,
                    receiver = chatMessageResponse.receiver,
                    message = chatMessageResponse.message,
                    date = chatMessageResponse.date
                )
            }

            withContext(Dispatchers.Main) {
                chatAdapter.setMessages(allMessages)
                var value = chatAdapter.getItemCount()
                recyclerViewMessages.scrollToPosition(value - 1)
            }
        }


        val buttonSend: ImageButton = findViewById(R.id.buttonSend)
        val editTextMessage: EditText = findViewById(R.id.editTextMessage)
        val buttonGift: ImageButton = findViewById(R.id.buttonGift)
        val buttonPlus: ImageButton = findViewById(R.id.buttonPlus)


        fun setButtonState(context: Context, editText: EditText, button: ImageButton) {
            val colorDisabled = ContextCompat.getColor(context, R.color.disabled)
            val colorBackDisabled = ContextCompat.getColor(context, R.color.backdisabled)
            val colorEnabled = ContextCompat.getColor(context, R.color.enabled)
            val colorBackEnabled = ContextCompat.getColor(context, R.color.backenabled)

            if (editText.text.toString().isEmpty()) {
                button.isEnabled = false
                button.setColorFilter(colorDisabled, PorterDuff.Mode.SRC_ATOP)
                button.backgroundTintList = ColorStateList.valueOf(colorBackDisabled)
                buttonPlus.visibility = View.VISIBLE
                buttonGift.visibility = View.VISIBLE
            } else {
                button.isEnabled = true
                button.setColorFilter(colorEnabled, PorterDuff.Mode.SRC_ATOP)
                button.backgroundTintList = ColorStateList.valueOf(colorBackEnabled)
                buttonPlus.visibility = View.GONE
                buttonGift.visibility = View.GONE
            }
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setButtonState(this@ChatMessageActivity, editTextMessage, buttonSend)
            }
        }


        editTextMessage.addTextChangedListener(textWatcher)

        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.time)

        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotBlank()) {
                val received = intent.getStringExtra("userid") ?: ""
                val newMessage = ChatMessage(currentUser, received, messageText, date)
                CoroutineScope(Dispatchers.IO).launch {
                    chatApiService.sendMessage(newMessage)

                    withContext(Dispatchers.Main) {
                        chatAdapter.addMessage(newMessage)
                        var value = chatAdapter.getItemCount()
                        recyclerViewMessages.scrollToPosition(value - 1)
                    }
                }
                editTextMessage.text.clear()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        fetchMessagesPeriodically()
    }

    private fun fetchMessagesPeriodically() {
        val intervalMillis = 1000
        val handler = Handler(Looper.getMainLooper())

        val fetchRunnable = object : Runnable {
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    val received = intent.getStringExtra("userid") ?: ""
                    val allMessagesResponse = chatApiService.getAllMessages(currentUser, received)
                    val allMessages = allMessagesResponse.map { chatMessageResponse ->
                        ChatMessage(
                            sender = chatMessageResponse.sender,
                            receiver = chatMessageResponse.receiver,
                            message = chatMessageResponse.message,
                            date = chatMessageResponse.date
                        )
                    }

                    withContext(Dispatchers.Main) {
                        chatAdapter.setMessages(allMessages)
                    }
                }
                handler.postDelayed(this, intervalMillis.toLong())
            }
        }

        handler.post(fetchRunnable)
    }
}
