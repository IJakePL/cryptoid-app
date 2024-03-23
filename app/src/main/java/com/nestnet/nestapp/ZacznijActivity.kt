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
import android.util.Patterns
import android.widget.Toast

class ZacznijActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zacznij_juz_teraz)

        val email_input: EditText = findViewById(R.id.email_input)
        val dswButton: ImageButton = findViewById(R.id.zjt_back)
        val logButton: Button = findViewById(R.id.zjt_kont)

        fun isEmailValid(email: CharSequence?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun register(context: Context, email: String) {
            if (isEmailValid(email)) {
                val email = email_input.text.toString()

                val intent = Intent(this, HasloActivity::class.java)
                intent.putExtra("EMAIL", email)

                startActivity(intent)
            } else {
                Toast.makeText(context, "Nieprawidłowy adres e-mail", Toast.LENGTH_SHORT).show()
            }
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
                    register(editText.context, editText.text.toString())
                }
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setButtonState(email_input, logButton)
            }
        }

        email_input.addTextChangedListener(textWatcher)

        dswButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
