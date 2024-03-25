package com.nestnet.nestapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.graphics.Color
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import android.widget.TextView

class NazwaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nazwa_zacznij)

        val nazwa_input: EditText = findViewById(R.id.nazwa_input)
        val dswButton: ImageButton = findViewById(R.id.zjt_back)
        val logButton: Button = findViewById(R.id.zacznij_kont)

        val textView1: TextView = findViewById(R.id.podany_email)
        val receivedEmail1 = intent.getStringExtra("EMAIL")
        textView1.text = receivedEmail1

        val textView2: TextView = findViewById(R.id.podane_haslo)
        val receivedEmail2 = intent.getStringExtra("HASŁO")
        textView2.text = receivedEmail2


        fun register() {
            val nazwa = nazwa_input.text.toString()

            val intent = Intent(this, PotwierdzActivity::class.java)
            intent.putExtra("HASŁO", receivedEmail2)
            intent.putExtra("EMAIL", receivedEmail1)
            intent.putExtra("NAZWA", nazwa)

            startActivity(intent)
        }

        fun setButtonState(editText: EditText, button: Button) {
            if (editText.text.toString().isEmpty()) {
                button.isEnabled = false
                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#7C7C7C"))
                button.setTextColor(Color.parseColor("#434343"))
                button.setOnClickListener(null)
            } else {
                button.isEnabled = true
                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6EC8FA"))
                button.setTextColor(Color.parseColor("#434343"))
                button.setOnClickListener {
                    register()
                }
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setButtonState(nazwa_input, logButton)
            }
        }

        nazwa_input.addTextChangedListener(textWatcher)

        dswButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
