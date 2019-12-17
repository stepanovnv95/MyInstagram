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

    @Delete
    fun delete(favorites: Favorites)
}
