package com.stepanovnv.myinstagram.data

import androidx.room.*


@Entity
data class Favorites(
    @PrimaryKey val post_id: Int,
    @ColumnInfo(name = "date") val date: String
)


@Dao
interface FavoritesDao {
    @Insert
    fun insert(vararg favorites: Favorites)

    @Query("SELECT * FROM favorites ORDER BY date")
    fun getFavorites(): List<Favorites>

    @Query("SELECT * FROM favorites WHERE post_id=:post_id LIMIT 1")
    fun checkFavorite(post_id: Int): Favorites?

    @Delete
    fun delete(favorites: Favorites)
}
