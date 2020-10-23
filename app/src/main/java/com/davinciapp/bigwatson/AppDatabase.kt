package com.davinciapp.bigwatson

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davinciapp.bigwatson.dao.AnalysisDao
import com.davinciapp.bigwatson.dao.UserDao
import com.davinciapp.bigwatson.view.analysis.Analysis
import com.davinciapp.bigwatson.model.TwitterUser

@Database(entities = arrayOf(TwitterUser::class, Analysis::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun analysisDao(): AnalysisDao

    companion object {
        //Singleton pattern
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(AppDatabase){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                return INSTANCE!!
            }
        }
    }

}