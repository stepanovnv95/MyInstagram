package com.stepanovnv.myinstagram.data

import android.content.Context
import androidx.room.*


@Entity
data class Post (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "like_status") val likeStatus: String = "none"
)


@Dao
interface PostDao {
    @Query("SELECT * from post WHERE id = :id LIMIT 1")
    fun getItemById(id: Int): Post?

    @Insert
    fun insert(vararg post: Post)

    @Query("UPDATE post SET like_status = :likeStatus WHERE id = :id")
    fun changeLikeStatus(id: Int, likeStatus: String)

    @Query("SELECT like_status from post WHERE id = :id LIMIT 1")
    fun getLikeStatus(id: Int): String
}


@Database(entities = arrayOf(Post::class), version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}


class PostDatabaseSingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: PostDatabaseSingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: PostDatabaseSingleton(context).also {
                        INSTANCE = it
                    }
            }
    }

    val db: PostDatabase by lazy {
        // TODO("Remove allowMainThreadQueries")
        Room.databaseBuilder(context, PostDatabase::class.java, "my-instagram").allowMainThreadQueries().build()
    }

}
