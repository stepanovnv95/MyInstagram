package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import android.content.ContextWrapper
import org.json.JSONObject


abstract class AbstractRequest(context: Context?, val onError: (String) -> Unit) : ContextWrapper(context) {
    abstract val url: String
}

abstract class PostRequest(context: Context?, val onResponse: (JSONObject) -> Unit, onError: (String) -> Unit)
    : AbstractRequest(context, onError) {
    abstract val params: Map<String, String>
}
