package com.nestnet.nestapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import android.widget.TextView
import android.widget.Toast

class HasloActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.haslo_zacznij)

        val dswButton: ImageButton = findViewById(R.id.zjt_back)
        val button: Button = findViewById(R.id.zacznij_kont)
        val textView: TextView = findViewById(R.id.podany_email)

        val receivedEmail = intent.getStringExtra("EMAIL")

        val haslo_input: EditText = findViewById(R.id.haslo_input)
        val haslo_potwierdz_input: EditText = findViewById(R.id.haslo_potwierdz_input)

        textView.text = receivedEmail

        fun register() {
            val haslo = haslo_input.text.toString()
            if (haslo == haslo_potwierdz_input.text.toString()) {
                val intent = Intent(this, NazwaActivity::class.java)
                intent.putExtra("HASŁO", haslo)
                intent.putExtra("EMAIL", receivedEmail)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Podane hasła nie są identyczne!", Toast.LENGTH_SHORT).show()
            }
        }

        fun setButtonState(haslo1: EditText, haslo2: EditText, button: Button) {
            val isEnabled = haslo1.text.isNotBlank() && haslo2.text.isNotBlank()
            button.isEnabled = isEnabled
            button.backgroundTintList = ColorStateList.valueOf(
                if (isEnabled) Color.parseColor("#6EC8FA") else Color.parseColor("#7C7C7C")
            )
            button.setTextColor(if (isEnabled) Color.parseColor("#434343") else Color.parseColor("#434343"))

            button.setOnClickListener {
                if (isEnabled) {
                    register()
                }
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setButtonState(haslo_input, haslo_potwierdz_input, button)
            }
        }

        haslo_potwierdz_input.addTextChangedListener(textWatcher)

        dswButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
