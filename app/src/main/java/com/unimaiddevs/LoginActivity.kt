package com.unimaiddevs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.signup
import kotlinx.android.synthetic.main.activity_registration.*
import life.sabujak.roundedbutton.RoundedButton
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog

class LoginActivity : AppCompatActivity() {
    private  var mAuth: FirebaseAuth? = null
    private var noInternetDialog: NoInternetDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mAuth = FirebaseAuth.getInstance();
        val login = findViewById<RoundedButton>(R.id.login)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val sharedPreference =  getSharedPreferences("data", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        val spannable = SpannableString("Don't have an account? Sign up")
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#00ACEE")),23,30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        signup.text = spannable
        resetpassword.setOnClickListener {

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        login.setOnClickListener {


            val  email = findViewById<EditText>(R.id.email)
            val Email = email.text.toString().trim()
            val password = findViewById<EditText>(R.id.password)
            val Password = password.text.toString().trim()

            if (Email.isEmpty()) {
                email.error = "Email Is Required"
                email.requestFocus()
                return@setOnClickListener
            }
            if (Password.isEmpty()) {
                password.error = "Password Is Required"
                password.requestFocus()
                return@setOnClickListener
            }
            internetAccess()
            progressBar?.visibility = View.VISIBLE;
            login.visibility = View.GONE
            mAuth?.signInWithEmailAndPassword(Email,Password)
                ?.addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val userId = mAuth!!.currentUser!!.uid
                        editor.putBoolean("registered",true)
                        editor.putString("userId",userId)
                        editor.apply()


                        progressBar?.visibility = View.INVISIBLE;
                        login.visibility = View.VISIBLE

                        // Sign in success, update UI with signed-in user's information\
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    } else {
                        progressBar?.visibility = View.INVISIBLE;
                        login.visibility = View.VISIBLE
                        Toast.makeText(this@LoginActivity, "Authentication failed PLease check your Email or Password",
                            Toast.LENGTH_LONG).show()
                    }
                }




        }


        signup.setOnClickListener {

            startActivity(Intent(this, RegistrationActivity::class.java))
        }

    }

    private fun internetAccess() {
        noInternetDialog = NoInternetDialog.Builder(this)
            .apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        // ...
                    }
                }
                cancelable = true // Optional
                noInternetConnectionTitle = "No Internet" // Optional
                noInternetConnectionMessage =
                    "Check your Internet connection and try again." // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = "Please turn on" // Optional
                wifiOnButtonText = "Wifi" // Optional
                mobileDataOnButtonText = "Mobile data" // Optional

                onAirplaneModeTitle = "No Internet" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                pleaseTurnOffText = "Please turn off" // Optional
                airplaneModeOffButtonText = "Airplane mode" // Optional
                showAirplaneModeOffButtons = true // Optional
            }
            .build()
    }

    override fun onBackPressed() {

        val sharedPreference = getSharedPreferences("data", Context.MODE_PRIVATE)
        val registered = sharedPreference.getBoolean("registered", true)
        Log.d("back",sharedPreference.getBoolean("registered", true).toString())
        if (!registered)
        {
            ActivityCompat.finishAffinity(this)
        }
        super.onBackPressed()
        //ActivityCompat.finishAffinity(this)
    }


}
