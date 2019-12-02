package com.stepanovnv.myinstagram.http

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class HttpQueueSingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: HttpQueueSingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: HttpQueueSingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

}