package com.stepanovnv.myinstagram.data

import androidx.room.*


@Entity
data class UserMeta(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "meta_key") val meta_key: String,
    @ColumnInfo(name = "value") val value: String
)


@Dao
interface UserDao {

    @Query("SELECT value FROM usermeta WHERE meta_key=:meta_key LIMIT 1")
    fun getKey(meta_key: String): String

    @Insert
    fun insert(vararg userMeta: UserMeta)

    @Query("UPDATE usermeta SET value=:value WHERE meta_key=:meta_key")
    fun updateKey(meta_key: String, value: String)
}