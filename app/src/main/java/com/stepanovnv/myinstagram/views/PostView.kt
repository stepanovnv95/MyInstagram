package com.stepanovnv.myinstagram.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.stepanovnv.myinstagram.R


class PostView(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post, this, true)
    }

    var url: String = "empty url"
}
