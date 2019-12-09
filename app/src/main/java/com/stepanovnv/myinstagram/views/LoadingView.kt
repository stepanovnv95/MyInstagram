package com.stepanovnv.myinstagram.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.stepanovnv.myinstagram.R


class LoadingView (context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_loading, this, true)
    }
}
