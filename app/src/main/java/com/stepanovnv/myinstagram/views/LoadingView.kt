package com.stepanovnv.myinstagram.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.stepanovnv.myinstagram.R


class LoadingView (context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)
    }
}
