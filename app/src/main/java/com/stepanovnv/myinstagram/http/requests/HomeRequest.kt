package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class HomeRequest(
    context: Context?,
    private val _lastId: Int? = null,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
)
    : PostRequest(context, onResponse, onError) {

    override val url: String = getString(R.string.url_home)
    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            if (_lastId != null)
                p["last_id"] = _lastId.toString()
            return p
        }

}
