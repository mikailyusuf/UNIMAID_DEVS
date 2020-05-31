package com.unimaiddevs

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


class EventsActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent

        val host = intent.getStringExtra("host")
        val venue = intent.getStringExtra("venue")
        val time = intent.getStringExtra("time")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("image")
        val link = intent.getStringExtra("link")
        val description = intent.getStringExtra("description")

        var Host: TextView = findViewById(R.id.host)
        var Venue: TextView = findViewById(R.id.venue)
        var Time: TextView = findViewById(R.id.time)
        var Date: TextView = findViewById(R.id.date)
        var Image = findViewById<ImageView>(R.id.image)
        var Link: TextView = findViewById(R.id.link)
        var Description: TextView = findViewById(R.id.description)


        //icons initialisation and visibility
        var timeicon: ImageView = findViewById(R.id.timeicon)
        var calendaricon: ImageView = findViewById(R.id.calendaricon)
        var locationicon: ImageView = findViewById(R.id.locationicon)
        var linkicon: ImageView = findViewById(R.id.linkicon)

        if (venue.isNullOrEmpty()) {
            locationicon.visibility = View.GONE
        }

        if (time.isNullOrEmpty()) {
            timeicon.visibility = View.GONE
        }
        if (link.isNullOrEmpty()) {
            linkicon.visibility = View.GONE
        }
        if (date.isNullOrEmpty()) {
            calendaricon.visibility = View.GONE
        }


        Host.text = host
        Venue.text = venue
        Time.text = time
        Date.text = date
        Link.text = link
        Description.text = description
        Picasso.get().load(image).into(Image)
        Link.setOnClickListener {

            openUri(link)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    //function to open the url links
    fun openUri(url: String) {

        var Url: String
        if (url == "") {
            Toast.makeText(this, "LINK IS EMPTY", Toast.LENGTH_SHORT).show()
        } else {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Url = "http://$url"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url)))
            }

            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
