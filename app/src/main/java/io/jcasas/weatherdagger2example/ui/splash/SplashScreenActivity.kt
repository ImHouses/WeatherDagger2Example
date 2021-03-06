package io.jcasas.weatherdagger2example.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import io.jcasas.weatherdagger2example.ui.main.MainActivity

// TODO: Register Activity on Manifest.
class SplashScreenActivity : AppCompatActivity() {

    // TODO: Add functionality for first time loading (Permissions request screen goes here).
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().run {
            Thread.sleep(1_500)
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}