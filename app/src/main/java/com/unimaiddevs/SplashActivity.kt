package com.unimaiddevs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val sharedPreference = getSharedPreferences("data", Context.MODE_PRIVATE)


        val animation = AnimationUtils.loadAnimation(this, R.anim.anim2)
        simage.startAnimation(animation)

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.anim1)
        stext.startAnimation(animation2)

        val registered = sharedPreference.getBoolean("registered", false)

        if (registered)
        {

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        else
        {
            Handler().postDelayed({

                startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
            }, 4000)
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }
}
