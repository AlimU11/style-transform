package com.example.styletransform

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flaviofaria.kenburnsview.KenBurnsView
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    private var timer = Timer()
    private lateinit var imageView: KenBurnsView
    private val colors = intArrayOf(R.color.starry_night, R.color.great_wave, R.color.black_spot, R.color.jalousie, R.color.profumo)
    private val images = intArrayOf(R.drawable.starry_night, R.drawable.great_wave, R.drawable.black_spot, R.drawable.jalousie, R.drawable.profumo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        supportActionBar?.hide()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        imageView = findViewById(R.id.imageSplash)

        val num = Random().nextInt(colors.size)

        imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, images[num], null))
        window.statusBarColor = ContextCompat.getColor(applicationContext, colors[num])

        timer.schedule(5000) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }
}