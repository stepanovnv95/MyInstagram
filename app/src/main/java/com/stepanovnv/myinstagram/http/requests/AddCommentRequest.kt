package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class AddCommentRequest(
    context: Context,
    private val postId: Int,
    private val userId: String,
    private val userName: String,
    private val commentText: String,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) : PostRequest(context, onResponse, onError) {

    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            p["post_id"] = postId.toString()
            p["user_id"] = userId
            p["user_name"] = userName
            p["comment"] = commentText
            return p
        }

    override val url: String = getString(R.string.url_addcomment)

}
