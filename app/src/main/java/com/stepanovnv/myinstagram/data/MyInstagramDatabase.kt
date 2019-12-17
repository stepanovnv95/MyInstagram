package com.stepanovnv.myinstagram.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Post::class, UserMeta::class, Favorites::class], version = 3, exportSchema = false)
abstract class MyInstagramDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun favoritesDao(): FavoritesDao
}


class MyInstagramDatabaseSingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: MyInstagramDatabaseSingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: MyInstagramDatabaseSingleton(context).also {
                        INSTANCE = it
                    }
            }
    }

    val db: MyInstagramDatabase by lazy {
        // TODO("Remove allowMainThreadQueries")
        Room.databaseBuilder(context, MyInstagramDatabase::class.java, "my-instagram").allowMainThreadQueries().build()
    }

}
