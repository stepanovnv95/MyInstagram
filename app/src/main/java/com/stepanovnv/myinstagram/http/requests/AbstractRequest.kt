package com.stepanovnv.myinstagram.http.requests


abstract class AbstractRequest {
    abstract val url: String
    abstract val params: Map<String, String>
}
