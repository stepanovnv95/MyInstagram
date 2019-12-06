package com.stepanovnv.myinstagram.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ComplexColorCompat
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.ImageRequest


class PostView(context: Context) : FrameLayout(context) {

    private val _baseTag = "PostView_"
    private var _tag: String = _baseTag
    private val _httpClient: HttpClient
    private var _imageView: ImageView
    var postData: PostData? = null
        set(value) {

            if (value != null) {
                _tag = _baseTag + value.id.toString()
                _httpClient.addRequest(ImageRequest(
                    context,
                    value.url,
                    { response ->
                        Log.d(_tag, "Image from %s loaded".format(value.url))
                        setImage(response)
                    },
                    { error -> Log.e(_tag, error) }
                ))
            }

            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post, this, true)
        _imageView = findViewById(R.id.imageView)
        _httpClient = HttpClient(_tag, context)
    }

    private fun setImage(image: Bitmap) {
        _imageView.setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.ADD)
        _imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        _imageView.setImageBitmap(image)
    }
}
