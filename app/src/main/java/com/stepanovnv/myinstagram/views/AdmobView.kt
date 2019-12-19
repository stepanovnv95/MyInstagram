package com.stepanovnv.myinstagram.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.stepanovnv.myinstagram.R


class AdmobView(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_admob, this, true)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder()
            .addTestDevice("6D26308B5DC0746B63DE049190F1665A")
            .build()
        adView.loadAd(adRequest)
    }

}
