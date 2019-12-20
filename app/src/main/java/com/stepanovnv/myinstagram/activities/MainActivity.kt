package com.stepanovnv.myinstagram.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.MyInstagramDatabaseSingleton
import com.stepanovnv.myinstagram.data.NotificationsDao
import com.stepanovnv.myinstagram.fragments.FavoritesFragment
import com.stepanovnv.myinstagram.fragments.HomeFragment
import com.stepanovnv.myinstagram.fragments.HotFragment
import com.stepanovnv.myinstagram.fragments.OtherFragment


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

}
