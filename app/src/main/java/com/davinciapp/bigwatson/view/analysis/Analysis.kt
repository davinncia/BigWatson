package com.davinciapp.bigwatson.view.analysis

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_table")
data class Analysis(
    @PrimaryKey
    @ColumnInfo(name = "user_id") val userId: Long,
    val openness: Int,
    val conscientiousness: Int,
    val extraversion: Int,
    val agreeableness: Int,
    @ColumnInfo(name = "emotional_range") val emotionalRange: Int

)