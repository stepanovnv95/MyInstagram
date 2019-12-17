package com.stepanovnv.myinstagram.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.FavoritesDao
import com.stepanovnv.myinstagram.data.MyInstagramDatabaseSingleton
import com.stepanovnv.myinstagram.http.requests.FavoritesRequest
import com.stepanovnv.myinstagram.http.requests.PostRequest
import org.json.JSONArray


class FavoritesFragment : BaseListFragment() {

    override val TAG: String = "FavoritesFragment"
    private lateinit var _favoritesDao: FavoritesDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _favoritesDao = MyInstagramDatabaseSingleton.getInstance(activity!!.applicationContext).db.favoritesDao()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun constructHttpRequest(): PostRequest {
        val favorites = _favoritesDao.getFavorites()
        val ja = JSONArray()
        for (f in favorites) {
            ja.put(f.post_id)
        }
        return FavoritesRequest(context!!, ja)
    }

    override fun getTitle(): String {
        return getString(R.string.title_favorites)
    }

}
