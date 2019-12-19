package com.stepanovnv.myinstagram.data

import androidx.room.*


@Entity
data class NotificationsMeta(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "meta_key") val meta_key: String,
    @ColumnInfo(name = "value") val value: String
)


@Dao
interface NotificationsDao {

    @Query("SELECT * FROM notificationsmeta WHERE meta_key=:meta_key LIMIT 1")
    fun getKey(meta_key: String): NotificationsMeta?

    @Insert
    fun insert(vararg notificationsMeta: NotificationsMeta)

    @Query("UPDATE notificationsmeta SET value=:value WHERE meta_key=:meta_key")
    fun updateKey(meta_key: String, value: String)

}
