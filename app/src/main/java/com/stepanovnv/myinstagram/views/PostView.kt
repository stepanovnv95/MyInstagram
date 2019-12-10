package com.stepanovnv.myinstagram.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.ImageRequest


class PostView(context: Context) : LinearLayout(context) {

    private val _baseTag = "PostView_"
    private var _tag: String = _baseTag
    private val _httpClient: HttpClient
    private val _imageView: ImageView

    private val _likeButton: View
    private val _likeImage: ImageView
    private val _likeCount: TextView
    private val _dislikeButton: View
    private val _dislikeImage: ImageView
    private val _dislikeCount: TextView

    var postData: PostData? = null
        set(value) {
            value ?: return

            _tag = _baseTag + value.id.toString()


            setImage(null)
            setLikes(value.likes, value.dislikes)

            _httpClient.addRequest(ImageRequest(
                context,
                value.url,
                { response -> setImage(response) },
                { error -> Log.e(_tag, error) }
            ))

            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_post, this, true)
        orientation = VERTICAL
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        _imageView = findViewById(R.id.imageView)
        _httpClient = HttpClient(_tag, context.applicationContext)

        _likeImage = findViewById(R.id.like_image)
        _likeCount = findViewById(R.id.like_text)
        _likeButton = findViewById(R.id.like_button)
        _dislikeImage = findViewById(R.id.dislike_image)
        _dislikeCount = findViewById(R.id.dislike_text)
        _dislikeButton = findViewById(R.id.dislike_button)
    }

    private fun setImage(image: Bitmap?) {
        if (image != null) {
            _imageView.setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.ADD)
            _imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            _imageView.setImageBitmap(image)
        } else {
            _imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            _imageView.scaleType = ImageView.ScaleType.CENTER
            _imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image_placeholder))
        }
    }

    private fun setLikes(likes: Int, dislikes: Int) {
        _likeCount.text = likes.toString()
        _dislikeCount.text = dislikes.toString()
    }

}
