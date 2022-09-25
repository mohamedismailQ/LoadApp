package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.BroadcastReceiver
import com.udacity.ui.ButtonState


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var fileTitle: String
        lateinit var downloadStatus: String
        lateinit var notificationManager: NotificationManager
        var DEFAULT_ANIMATION_DURATION = 2000L
    }

    private var downloadID: Long = 0
    private var Urls: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

        custom_button.setOnClickListener {
            // onClickListener for custom btn and we check the radio group by checkedRadioButtonId
            when (radio_group.checkedRadioButtonId) {
                // for glide we put url for downloading and file title and we call fun download() to download files from internet
                // in last we make button state update for loading state
                R.id.glide_id -> {
                    Urls = "https://github.com/bumptech/glide/archive/master.zip"
                    fileTitle = getString(R.string.glide_library)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                }
                // for project load app we put url for downloading and file title and we call fun download() to download files from internet
                // in last we make button state update for loading state
                R.id.project_id -> {
                    Urls  = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    fileTitle = getString(R.string.load_app_project)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                }
                // for retrofit we put url for downloading and file title and we call fun download() to download files from internet
                // in last we make button state update for loading state
                R.id.retrofit_id -> {
                    Urls = "https://github.com/square/retrofit/archive/master.zip"
                    fileTitle = getString(R.string.retrofit_library)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                }
               // here we say if not found any checked radio btn we tell the user to check one
                else -> {
                    Toast.makeText(this, "Select One To Download It", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    // here is receiver for our request
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val Rec = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            createNotificationChannel(this@MainActivity)

            if (Rec == downloadID) {
                downloadStatus = ""
                Toast.makeText(context, getString(R.string.download_completed),Toast.LENGTH_SHORT).show()
                val downloadManagerServ = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor: Cursor = downloadManagerServ.query(DownloadManager.Query().setFilterById(downloadID))
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        // if download failed we put extra for the user in detail activity
                        DownloadManager.STATUS_FAILED -> {
                            downloadStatus = getString(R.string.failed_status)
                            custom_button.buttonState = ButtonState.Completed
                        }
                        // if download successfully we put extra for the user in detail activity
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloadStatus = getString(R.string.success_status)
                            custom_button.buttonState = ButtonState.Completed
                            // and we update state for btn to completed to make btn work again
                        }
                    }
                    // we here done with notification and pass to it urls
                    // that we need to download if checked and status from every file
                    notificationManager.sendNotification(
                        Urls,
                        downloadStatus,
                        this@MainActivity
                    )
                }
            }
        }
    }
    // fun download() for downloading files by passing to it urls as a param
    private fun download() {
        val requestedUrl =
            DownloadManager.Request(Uri.parse(Urls))
                // add some orders if file was gonna to download
                // as we need to download files even the phone is not in charging as a example
                .setTitle(fileTitle)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(requestedUrl)
    }







}
