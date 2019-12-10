package com.stepanovnv.myinstagram.data

import android.content.Context
import org.json.JSONObject


class PostData(
    applicationContext: Context,
    val id: Int,
    val url:String,
    likes: Int,
    dislikes: Int
) {
    companion object {
        fun fromJson(applicationContext: Context, json: JSONObject): PostData {
            val id = json.getInt("id")
            val url = json.getString("image_url")
            val likes = json.getInt("likes")
            val dislikes = json.getInt("dislikes")
            return PostData(applicationContext, id, url, likes, dislikes)
        }
    }

    var likes = likes
//        get() = if (isLiked && field == 0) 1 else field

    var dislikes = dislikes
//        get() = if (isDisliked && field == 0) 1 else field

    var isLiked = false
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                likeStatus = "like"
                isDisliked = false
                ++likes
            } else {
                if (likeStatus == "like")
                    likeStatus = "none"
                --likes
            }
        }
        get() = _postDao.getLikeStatus(id) == "like"

    var isDisliked = false
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                likeStatus = "dislike"
                isLiked = false
                ++dislikes
            } else {
                if (likeStatus == "dislike")
                    likeStatus = "none"
                --dislikes
            }
        }
        get() = _postDao.getLikeStatus(id) == "dislike"

    private var likeStatus: String = "none"
        set(value) {
            if (field == value) return
            field = value
            _postDao.changeLikeStatus(id, value)
        }


    private val _postDao = PostDatabaseSingleton.getInstance(applicationContext).db.postDao()
    init {
        if (_postDao.getItemById(id) == null) {
            _postDao.insert(Post(id))
        }
        likeStatus = _postDao.getLikeStatus(id)
        isLiked = (likeStatus == "like")
        isDisliked = (likeStatus == "dislike")
    }


//    interface PostDataListener {
//        fun onPostDataIsLikedChanged(isLiked: Boolean)
//        fun onPostDataLikesChanged(likes: Int)
//        fun onPostDataIsDislikedChanged(isLiked: Boolean)
//        fun onPostDataDislikesChanged(likes: Int)
//    }
//
//    private val _subscribers = ArrayList<PostDataListener>()
//
//    fun addSubscriber(listener: PostDataListener) {
//        _subscribers.add(listener)
//    }
//
//    fun removeSubscriber(listener: PostDataListener) {
//        var i = 0
//        val dSizeBefore = _subscribers.size
//        while (i < _subscribers.size) {
//            if (_subscribers[i] == listener)
//                _subscribers.removeAt(i)
//            else
//                ++i
//        }
//        Log.d("removeSubscriber", "%d -> %d".format(dSizeBefore, _subscribers.size))
//    }

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return id
    }
}
