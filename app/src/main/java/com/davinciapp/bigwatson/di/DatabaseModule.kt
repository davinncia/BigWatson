package com.davinciapp.bigwatson.di

import android.content.Context
import com.davinciapp.bigwatson.repository.AnalysisRepo
import com.davinciapp.bigwatson.repository.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext appContext: Context) = UserRepo(appContext)

    @Provides
    @Singleton
    fun provideAnalysisRepository(@ApplicationContext appContext: Context) = AnalysisRepo(appContext)
}