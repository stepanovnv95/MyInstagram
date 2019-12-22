package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class HotRequest(
    context: Context?,
    private val _lastIndex: Int? = null,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) : PostRequest(context, onResponse, onError) {

    override val url: String = getString(R.string.url_hot)

    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            if (_lastIndex != null)
                p["last_index"] = _lastIndex.toString()
            return p
        }

}
