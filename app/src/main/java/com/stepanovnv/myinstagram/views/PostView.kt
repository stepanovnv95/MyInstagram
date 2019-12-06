package com.stepanovnv.myinstagram.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.stepanovnv.myinstagram.R


class PostView(context: Context) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post, this, true)
        orientation = VERTICAL
    }

    var url: String = "empty url"
        set(value) {
            findViewById<TextView>(R.id.test_image_url).text = value
            field = value
        }
}
