package com.example.locationalarm.di

import android.content.Context
import androidx.room.Room
import com.example.locationalarm.data.local.AlarmDao
import com.example.locationalarm.data.local.AlarmDatabase
import com.example.locationalarm.data.repository.AlarmRepositoryImpl
import com.example.locationalarm.domain.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "alarm_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmDao(database: AlarmDatabase): AlarmDao {
        return database.alarmDao()
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository {
        return AlarmRepositoryImpl(alarmDao)
    }
}
