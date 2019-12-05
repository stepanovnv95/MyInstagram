package com.stepanovnv.myinstagram.http.requests


class HomeRequest(private val _lastId: Int? = null)
    : AbstractRequest() {

    override val url: String = "https://reactor.pbip.ru/nikita4you/scripts/get_all_post.php"
//    override val url: String = "http://v-wall.net/test.php"
    override val params: Map<String, String>
        get() {
            val p = HashMap<String, String>()
            if (_lastId != null)
                p["last_id"] = _lastId.toString()
            return p
        }

}
