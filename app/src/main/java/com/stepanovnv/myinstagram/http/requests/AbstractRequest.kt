package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import android.content.ContextWrapper
import org.json.JSONObject


abstract class AbstractRequest(
    context: Context?,  // TODO("What for is need the context?")
    var onError: ((String) -> Unit)? = null
) : ContextWrapper(context) {
    abstract val url: String
}

abstract class PostRequest(
    context: Context?,
    var onResponse: ((JSONObject) -> Unit)? = null,
    onError: ((String) -> Unit)? = null
)
    : AbstractRequest(context, onError) {
    abstract val params: Map<String, String>
}
