package com.davinciapp.bigwatson.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.davinciapp.bigwatson.AppDatabase
import com.davinciapp.bigwatson.dao.AnalysisDao
import com.davinciapp.bigwatson.view.analysis.Analysis
import javax.inject.Inject

class AnalysisRepo @Inject constructor(context: Context) {

    private val analysisDao: AnalysisDao

    init {
        val db = AppDatabase.getDatabase(context)
        analysisDao = db.analysisDao()
    }

    suspend fun getAnalysis(userId: Long): Analysis = analysisDao.getAnalysis(userId)

    suspend fun insert(analysis: Analysis) = analysisDao.insert(analysis)

    suspend fun delete(userId: Long) = analysisDao.delete(userId)
}