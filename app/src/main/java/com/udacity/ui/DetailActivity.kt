package com.udacity.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    //Here we create the variables
    // that carry the extra from the notification
    // and it is important that we be sensitive to the case of the letters
    private var file = "FileName"
    private var downloadStatus = "Status"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        // we can call views that well shown what we need to show here
        // from xml to kotlin by call
        // view id and pass to it what we well show
        file_name_id.text = intent.getStringExtra(file)
        status_name_id.text = intent.getStringExtra(downloadStatus)
        // onClickListener for fab button
        fab.setOnClickListener { finish() }
    }
}
