package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import com.stepanovnv.myinstagram.R
import org.json.JSONObject


class HotRequest(
    context: Context?,
    lastId: Int? = null,
    onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) : HomeRequest(context, lastId, onResponse, onError) {

    override val url: String = getString(R.string.url_hot)

}
