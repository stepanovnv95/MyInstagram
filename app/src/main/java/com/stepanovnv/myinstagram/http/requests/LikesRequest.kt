package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class LikesRequest(
    appContext: Context?,
    private val id: Int,
    private val like: Int,
    private val dislike: Int,
    onResponse: ((JSONObject) -> Unit)?,
    onError: ((String) -> Unit)?
) : PostRequest(appContext, onResponse, onError) {
    override val url: String = getString(R.string.url_like)
    override val params: Map<String, String>
        get() {
            val r = -1..1
            if (like !in r || dislike !in r) {
                throw  Exception("Like and dislike should has integer value in range [-1;1]")
            }
            val p = HashMap<String, String>()
            p["id"] = id.toString()
            if (like != 0)
                p["like"] = like.toString()
            if (dislike != 0)
                p["dislike"] = dislike.toString()
            return p
        }
}
