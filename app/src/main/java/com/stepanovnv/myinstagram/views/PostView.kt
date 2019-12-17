package com.stepanovnv.myinstagram.views

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.stepanovnv.myinstagram.activities.CommentsActivity
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.ImageRequest


class PostView(context: Context) : LinearLayout(context)/*, PostData.PostDataListener*/ {

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
    private val _commentsButton: View

    var postData: PostData? = null
        set(value) {
//            if (field != null) {
//                field!!.removeSubscriber(this)
//            }

            if (value != null) {
                _tag = _baseTag + value.id.toString()
                setLikes(value.likes, value.dislikes)
//                value.addSubscriber(this)
                _httpClient.addRequest(ImageRequest(
                    context,
                    value.url,
                    { response -> setImage(response) },
                    { error -> Log.e(_tag, error) }
                ))
            } else {
                tag = _baseTag
            }

            setImage(null)

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
        _commentsButton = findViewById(R.id.comments_button)

        _likeButton.setOnClickListener {
            postData?.setLiked( ! postData!!.isLiked() )
            updateLikes()
        }

        _dislikeButton.setOnClickListener {
            postData?.setDisliked( ! postData!!.isDisliked() )
            updateLikes()
        }

        _commentsButton.setOnClickListener {
            openComments()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateLikes()
    }

    private fun updateLikes() {
        val isLiked = postData!!.isLiked()
        val isDisliked = postData!!.isDisliked()

        val likeColor = ContextCompat.getColor(
            context,
            if (isLiked) R.color.colorAccent else R.color.colorPrimaryDark
        )
        val dislikeColor = ContextCompat.getColor(
            context,
            if (isDisliked) R.color.colorAccent else R.color.colorPrimaryDark
        )

        _likeCount.setTextColor(likeColor)
        _likeImage.setColorFilter(likeColor)
        _dislikeCount.setTextColor(dislikeColor)
        _dislikeImage.setColorFilter(dislikeColor)

        var likesCount = postData!!.likes
        if (isLiked && likesCount == 0) likesCount = 1
        var dislikesCount = postData!!.dislikes
        if (isDisliked && dislikesCount == 0) dislikesCount = 1
        _likeCount.text = likesCount.toString()
        _dislikeCount.text = dislikesCount.toString()
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

    private fun openComments() {
        val intent = Intent(context as Activity, CommentsActivity::class.java)
        intent.putExtra("POST_ID", postData!!.id)
        context.startActivity(intent)
    }

}
