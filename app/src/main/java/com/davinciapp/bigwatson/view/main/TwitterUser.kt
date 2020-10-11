package com.davinciapp.bigwatson.view.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TwitterUser(
    val id: Long,
    val imageUrl: String,
    val displayName: String,
    val isVerified: Boolean
): Parcelable