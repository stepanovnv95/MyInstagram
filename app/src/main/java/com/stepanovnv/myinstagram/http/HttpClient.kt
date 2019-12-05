package com.stepanovnv.myinstagram.http

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.stepanovnv.myinstagram.http.requests.AbstractRequest
import org.json.JSONException
import org.json.JSONObject


class HttpClient(tag: String, context: Context) {

    private val _tag: String = "%s_%s".format("HttpClient", tag)
    private val _context: Context = context

    fun addRequest(request: AbstractRequest, onResponse: (JSONObject) -> Unit, onError: (String) -> Unit) {
        val r = object : StringRequest(
            Method.POST, request.url,
            Response.Listener { response ->
                try {
                    onResponse(JSONObject(response))
                } catch (e: JSONException) {
                    onError(e.toString())
                }
            },
            Response.ErrorListener { error ->
                onError(error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return request.params as MutableMap<String, String>
            }
        }
        r.tag = _tag
        r.setShouldCache(false)
        val queue = HttpQueueSingleton.getInstance(_context).requestQueue
        queue.add(r)
    }
}
