package com.stepanovnv.myinstagram.data

import android.util.Log
import org.json.JSONObject


class PostData(
    val id: Int,
    val url:String,
    var likes: Int,
    var dislikes: Int
) {
    companion object {
        fun fromJson(json: JSONObject): PostData {
            val id = json.getInt("id")
            val url = json.getString("image_url")
            val likes = json.getInt("likes")
            val dislikes = json.getInt("dislikes")
            return PostData(id, url, likes, dislikes)
        }
    }

    var isLiked = false
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                isDisliked = false
                ++likes
            } else
                --likes
        }

    var isDisliked = false
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                isLiked = false
                ++dislikes
            } else
                --dislikes
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
