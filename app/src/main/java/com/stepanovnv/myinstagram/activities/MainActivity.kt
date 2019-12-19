package com.stepanovnv.myinstagram.activities

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
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

    companion object {
        private var lastSelectedItemId: Int? = null

        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val default_notification_channel_id = "default"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (_isFragmentsInit)
            return
        else
            _isFragmentsInit = true

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

//        setAlarm()
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
        lastSelectedItemId = menuItem.itemId
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun setAlarm() {

        val checkCalendar = Calendar.getInstance()
        val now = checkCalendar.time
        val simpleDateFormat = SimpleDateFormat("dd")
        val timestamp: String = simpleDateFormat.format(now)
        val notificationMeta = _notificationsDao.getKey("last_notification")
        if (notificationMeta == null) {
            _notificationsDao.insert(NotificationsMeta(0, "last_notification", ""))
        } else if (notificationMeta.value == timestamp) {
            return
        }
        _notificationsDao.updateKey("last_notification", timestamp)

        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
//        val notification = getNotification()
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(this, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, getString(R.string.push_notification_hour).toInt())
            set(Calendar.MINUTE, getString(R.string.push_notification_minute).toInt())
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    class MyNotificationPublisher : BroadcastReceiver() {

        companion object {
            val NOTIFICATION = "notification"
            val NOTIFICATION_ID = "notification-id"
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

        override fun onReceive(context: Context?, intent: Intent?) {
            val NOTIFICATION_CHANNEL_ID = "10001"

            val notificationManager = context!!
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val notification = intent!!.getParcelableExtra<Notification>(NOTIFICATION)
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

}
