package com.stepanovnv.myinstagram.data

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

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return id
    }
}
