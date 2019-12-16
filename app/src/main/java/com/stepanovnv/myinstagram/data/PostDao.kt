package com.stepanovnv.myinstagram.data

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
