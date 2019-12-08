package com.stepanovnv.myinstagram.data

import org.json.JSONObject


class PostData(
    val id: Int,
    val url:String
) {
    companion object {
        fun fromJson(json: JSONObject): PostData {
            val id = json.getInt("id")
            val url = json.getString("image_url")
            return PostData(id, url)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return id
    }
}
