package com.nestnet.nestapp.ui.theme

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.nestnet.nestapp.MainActivity
import org.json.JSONObject
import java.io.File
import java.io.FileReader

private val DarkColorScheme = darkColorScheme(
    onPrimary = Gray,
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    onPrimary = Gray,
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

var themeColor =""

@Composable
fun NestappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val file = File(LocalContext.current.filesDir, "theme.json")

    if (file.exists()) {
        try {
            val fileReader = FileReader(file)
            val jsonString = fileReader.readText()

            if (jsonString.isNotEmpty()) {
                val jsonObject = JSONObject(jsonString)
                val newThemeColor = jsonObject.getString("theme")
                if (themeColor != newThemeColor) {
                    themeColor = newThemeColor
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(LocalContext.current, MainActivity::class.java)
            LocalContext.current.startActivity(intent)
            (LocalContext.current as Activity).finish()
        }
    }

    val colorScheme = if (themeColor == "Ciemny") {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.statusBarColor = DarkColorScheme.onPrimary.toArgb()
            } else {
                window.statusBarColor = DarkColorScheme.onPrimary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}