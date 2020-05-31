package com.unimaiddevs

//import jdk.nashorn.internal.runtime.ECMAException.getException

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Html.fromHtml
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_registration.*
import life.sabujak.roundedbutton.RoundedButton
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog


//import org.junit.experimental.results.ResultMatchers.isSuccessful


class RegistrationActivity : AppCompatActivity() {

    var db = Firebase.firestore
    private var noInternetDialog: NoInternetDialog? = null
    val data:String = "User_Data"
    private val TAG = "REGISTRATION ACTIVITY"
    private var mAuth: FirebaseAuth? = null
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Users")

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val sharedPreference =  getSharedPreferences("data", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        mAuth = FirebaseAuth.getInstance();
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val spannable = SpannableString("Already Registered? Log in")
      spannable.setSpan(ForegroundColorSpan(Color.parseColor("#00ACEE")),19,26,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        login.text = spannable
        val signup: RoundedButton = findViewById<RoundedButton>(R.id.signup)
        login.setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))
        }

        signup.setOnClickListener {


            val email = findViewById<EditText>(R.id.email)
            val Email: String = email.text.toString().trim()

            val name = findViewById<EditText>(R.id.name)
            val Name: String = name.text.toString().trim()
            val phonenumber = findViewById<EditText>(R.id.phonenumber)
            val Phonenumber = phonenumber.text.toString().trim()
            val password = findViewById<EditText>(R.id.password)
            val Password = password.text.toString().trim()


            if (Email.isNullOrEmpty()) {
                Log.d("TESTTING", "this is the  email $Email")
                email.error = "Email Is Required"
                email.requestFocus()
                return@setOnClickListener

            }

            if (Name.isEmpty()) {
                name.error = "Name Is Required"
                name.requestFocus()
                return@setOnClickListener
            }

            if (Phonenumber.isEmpty()) {
                phonenumber.error = "Phonenumber Is Required"
                phonenumber.requestFocus()
                return@setOnClickListener
            }
            if (Password.isEmpty()) {
                password.error = "Password Is Required"
                password.requestFocus()
                return@setOnClickListener
            }
            progressBar?.visibility = View.VISIBLE;
            signup.visibility = View.GONE
            internetAccess()
            mAuth!!.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information

                        val userId = mAuth!!.currentUser!!.uid

                        Log.d("USERID",userId)
                        createUser(Email, Name, Phonenumber, userId)
                        editor.putBoolean("registered",true)
                        editor.putString("userId",userId)
                        editor.apply()
                        progressBar?.visibility = View.GONE;
                        Snackbar.make(layout, "REGISTRATION SUCCESSFULL", Snackbar.LENGTH_LONG)
                            .show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                    } else { // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Cant register User PLease Check your email or Password.", Toast.LENGTH_LONG).show()
                        signup.visibility = View.VISIBLE
                        progressBar?.visibility = View.GONE;

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

    private fun createUser(email: String, name: String, mobile: String, Userid: String) {
        val user = UserModel(email,name, mobile)
        myRef.child(Userid).setValue(user)
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }
}
