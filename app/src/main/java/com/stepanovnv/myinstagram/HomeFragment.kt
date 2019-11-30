package com.stepanovnv.myinstagram

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment


class HomeFragment : ListFragment() {

    private val catNames = listOf("Рыжик", "Барсик", "Мурзик",
        "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка",
        "Кузя", "Китти", "Масяня", "Симба")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = PostAdapter(activity as Context, android.R.layout.simple_list_item_1, catNames)
        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

}
