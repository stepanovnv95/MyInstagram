package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class GetCommentsRequest(
    context: Context,
    private val post_id: Int,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) : PostRequest(context, onResponse, onError) {

    override val url: String = getString(R.string.url_getcomments)

    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            p["id"] = post_id.toString()
            return p
        }
}
