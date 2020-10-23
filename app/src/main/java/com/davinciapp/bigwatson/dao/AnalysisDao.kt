package com.davinciapp.bigwatson.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.davinciapp.bigwatson.view.analysis.Analysis

@Dao
interface AnalysisDao {

    @Query("SELECT * FROM analysis_table WHERE  user_id = :userId")
    suspend fun getAnalysis(userId: Long): Analysis

    @Insert
    suspend fun insert(analysis: Analysis)

    @Query("DELETE FROM analysis_table WHERE user_id = :userId")
    suspend fun delete(userId: Long)

}