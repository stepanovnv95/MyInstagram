package com.stepanovnv.myinstagram.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.LikesRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class PostData(
    private val applicationContext: Context,
    val id: Int,
    val url:String,
    var likes: Int,
    var dislikes: Int,
    var comments: Int
) {
    companion object {
        fun fromJson(applicationContext: Context, json: JSONObject): PostData {
            val id = json.getInt("id")
            val url = json.getString("image_url")
            val likes = json.getInt("likes")
            val dislikes = json.getInt("dislikes")
            val comments = json.getInt("comments")
            return PostData(applicationContext, id, url, likes, dislikes, comments)
        }
    }

    private val _tag = "PostData_%d".format(id)
    private val _postDao = MyInstagramDatabaseSingleton.getInstance(applicationContext).db.postDao()
    private val _favoritesDao = MyInstagramDatabaseSingleton.getInstance(applicationContext).db.favoritesDao()
    private val _httpClient = HttpClient(_tag, applicationContext)

    init {
        if (_postDao.getItemById(id) == null) {
            _postDao.insert(Post(id))
        }
    }

    fun isFavorited(): Boolean {
        val f = _favoritesDao.checkFavorite(id)
        return (f != null)
    }

    fun setFavorited(favorite: Boolean) {
        if (favorite) {
            val calendar = Calendar.getInstance()
            val now = calendar.time
            @SuppressLint("SimpleDateFormat")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            val timestamp: String = simpleDateFormat.format(now)
            _favoritesDao.insert(Favorites(id, timestamp))
        } else
            _favoritesDao.delete(Favorites(id, ""))
    }

    fun isLiked(): Boolean {
        return _postDao.getLikeStatus(id) == "like"
    }

    fun isDisliked(): Boolean {
        return _postDao.getLikeStatus(id) == "dislike"
    }

    fun setLiked(value: Boolean) {
        val isDisliked = isDisliked()

        _httpClient.addRequest(LikesRequest(
            applicationContext,
            id,
            if (value) 1 else - 1,
            if (isDisliked) -1 else 0,
            { response -> Log.d(_tag, "Like response is ok: %s".format(response.toString())) },
            { error -> Log.e(_tag, "Like response failed: %s".format(error)) }
        ))

        _postDao.changeLikeStatus(id, if (value) "like" else "none")
        if (value) {
            ++likes
            if (isDisliked && dislikes > 0) --dislikes
        } else if (likes > 0)
            --likes
    }

    fun setDisliked(value: Boolean) {
        val isLiked = isLiked()

        _httpClient.addRequest(LikesRequest(
            applicationContext,
            id,
            if (isLiked) -1 else 0,
            if (value) 1 else - 1,
            { response -> Log.d(_tag, "Dislike response is ok: %s".format(response.toString())) },
            { error -> Log.e(_tag, "Dislike response failed: %s".format(error)) }
        ))

        _postDao.changeLikeStatus(id, if (value) "dislike" else "none")
        if (value) {
            ++dislikes
            if (isLiked && likes > 0) --likes
        } else if (dislikes > 0)
            --dislikes
    }


    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return id
    }
}
