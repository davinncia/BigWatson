package com.davinciapp.bigwatson.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class TwitterUser(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "is_verified") val isVerified: Boolean
): Parcelable