package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import android.util.Log
import com.stepanovnv.myinstagram.R
import org.json.JSONArray
import org.json.JSONObject


class FavoritesRequest (
    context: Context,
    private val posts: JSONArray,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) : PostRequest(context, onResponse, onError) {

    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            p["posts"] = posts.toString()
            Log.d("FavoritesRequest", p.toString())
            return p
        }

    override val url: String = getString(R.string.url_favorites)

}
