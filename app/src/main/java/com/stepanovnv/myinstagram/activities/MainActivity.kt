package com.stepanovnv.myinstagram.activities

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.MyInstagramDatabaseSingleton
import com.stepanovnv.myinstagram.data.NotificationsDao
import com.stepanovnv.myinstagram.data.NotificationsMeta
import com.stepanovnv.myinstagram.fragments.FavoritesFragment
import com.stepanovnv.myinstagram.fragments.HomeFragment
import com.stepanovnv.myinstagram.fragments.HotFragment
import com.stepanovnv.myinstagram.fragments.OtherFragment
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var _menu: BottomNavigationView
    private lateinit var _homeFragment: Fragment
    private lateinit var _hotFragment: Fragment
    private lateinit var _favoritesFragment: Fragment
    private lateinit var _otherFragment: Fragment
    private var _isFragmentsInit: Boolean = false

    private lateinit var _notificationsDao: NotificationsDao

    private lateinit var _interstitialAd: InterstitialAd

    companion object {
        private var lastSelectedItemId: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (_isFragmentsInit)
            return
        else
            _isFragmentsInit = true

        MobileAds.initialize(this) {}
        _interstitialAd = InterstitialAd(this)
        _interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        _interstitialAd.loadAd(
            AdRequest.Builder()
                .addTestDevice("6D26308B5DC0746B63DE049190F1665A")
                .build()
        )
        _interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                _interstitialAd.loadAd(
                    AdRequest.Builder()
                        .addTestDevice("6D26308B5DC0746B63DE049190F1665A")
                        .build()
                )
            }
        }
        _notificationsDao = MyInstagramDatabaseSingleton.getInstance(applicationContext).db.notificationsDao()

        setContentView(R.layout.activity_main)

        _menu = findViewById(R.id.bottom_nav_view)
        _homeFragment = HomeFragment()
        _hotFragment = HotFragment()
        _favoritesFragment = FavoritesFragment()
        _otherFragment = OtherFragment()

        if (lastSelectedItemId == null)
            lastSelectedItemId = R.id.action_home

        _menu.setOnNavigationItemSelectedListener { menuItem -> onMenuSelected(menuItem) }
        _menu.selectedItemId = lastSelectedItemId!!

        AlarmHelper.setAlarm(this, false)
    }

    private fun onMenuSelected(menuItem: MenuItem): Boolean {
        val fragment = when (menuItem.itemId) {
            R.id.action_home -> _homeFragment
            R.id.action_hot -> _hotFragment
            R.id.action_favorites -> _favoritesFragment
            R.id.action_other -> _otherFragment
            else -> null
        }
        fragment ?: return false

        if (_interstitialAd.isLoaded && (0..100).random() < getString(R.string.ad_chance).toInt()) {
            _interstitialAd.show()
        }

        lastSelectedItemId = menuItem.itemId
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }



    class BootReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action == "android.intent.action.BOOT_COMPLETED")
                AlarmHelper.setAlarm(context!!, true)
        }

    }


    class MyNotificationPublisher : BroadcastReceiver() {
        companion object {
            const val NOTIFICATION_ID = "notification-id"
            const val NOTIFICATION_CHANNEL_ID = "10001"
            const val default_notification_channel_id = "default"
        }

        private fun getNotification (context: Context): Notification {

            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder( context, default_notification_channel_id )
            builder.setContentTitle(context.getString(R.string.app_name))
            builder.setContentText(context.getString(R.string.push_notification))
            builder.setSmallIcon(R.drawable.ic_like )
            builder.setAutoCancel( true )
            builder.setChannelId( NOTIFICATION_CHANNEL_ID )
            builder.setSound(sound)
            builder.setContentIntent(pendingIntent)
            return builder.build()
        }

        @SuppressLint("SimpleDateFormat")
        override fun onReceive(context: Context?, intent: Intent?) {
            val checkCalendar = Calendar.getInstance()
            val now = checkCalendar.time
            val simpleDateFormat = SimpleDateFormat("dd")
            val timestamp: String = simpleDateFormat.format(now)
            val notificationsDao = MyInstagramDatabaseSingleton.getInstance(context!!.applicationContext)
                .db.notificationsDao()
            notificationsDao.updateKey("last_notification", timestamp)

            val lastOpened = notificationsDao.getKey("last_opened")
            if (lastOpened!!.value == timestamp)
                return

            val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = getNotification(context)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "NOTIFICATION_CHANNEL_NAME",
                    importance
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val id = intent!!.getIntExtra(NOTIFICATION_ID, 0)
            notificationManager.notify(id, notification)
        }
    }

    class AlarmHelper() {
        companion object {

            @SuppressLint("SimpleDateFormat")
            fun setAlarm(context: Context, notifyToday: Boolean) {
                val checkCalendar = Calendar.getInstance()
                val now = checkCalendar.time
                val simpleDateFormat = SimpleDateFormat("dd")
                val timestamp: String = simpleDateFormat.format(now)

                val notificationsDao = MyInstagramDatabaseSingleton.getInstance(context.applicationContext)
                    .db.notificationsDao()

                if (!notifyToday) {
                    val lastOpened = notificationsDao.getKey("last_opened")
                    if (lastOpened == null) {
                        notificationsDao.insert(NotificationsMeta(1, "last_opened", timestamp))
                    } else if (lastOpened.value != timestamp) {
                        notificationsDao.updateKey("last_opened", timestamp)
                    }
                }

                val lastAlarm = notificationsDao.getKey("last_notification")
                if (lastAlarm == null) {
                    notificationsDao.insert(NotificationsMeta(0, "last_notification", timestamp))
                } else if (lastAlarm.value == timestamp) {
                    Log.d("push_notification", "Alarm not created")
                    return
                }
                Log.d("push_notification", "Alarm created")

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
                notificationIntent.action = context.getString(R.string.alarm_key)
                val alarmIntent = PendingIntent.getBroadcast(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, context.getString((R.string.push_notification_hour)).toInt())
                    set(Calendar.MINUTE, context.getString(R.string.push_notification_minute).toInt())
                }

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
                )
            }

        }
    }

}
