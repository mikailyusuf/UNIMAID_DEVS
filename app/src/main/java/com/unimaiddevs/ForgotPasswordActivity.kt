package com.unimaiddevs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgotpassword.*
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog

class ForgotPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
//...
private var noInternetDialog: NoInternetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)

        resetpassword.setOnClickListener {
            internetAccess()
            progressBar.visibility = View.VISIBLE
        mAuth!!.sendPasswordResetEmail(email.toString())

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetpassword.visibility = View.INVISIBLE
                  Toast.makeText(this,"RESET  PASSWORD EMAIL LINK SENT SUCCESSFULL",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    // failed!
                    resetpassword.visibility = View.VISIBLE
                    Toast.makeText(this,"PLEASE CHECK YOUR EMAIL OR TRY AGAIN",Toast.LENGTH_LONG).show()
                }
            }

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
}
