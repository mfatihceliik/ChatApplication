package com.mfatihceliik.chatapplication.di

import android.content.Context
import androidx.room.Room
import com.mfatihceliik.chatapplication.data.local.ChatAppDao
import com.mfatihceliik.chatapplication.data.local.ChatApplicationDatabase
import com.mfatihceliik.chatapplication.data.local.LocalDataSource
import com.mfatihceliik.chatapplication.data.local.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideSharedPreferenceManager(@ApplicationContext context: Context): SharedPreferenceManager {
        return SharedPreferenceManager(context = context)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(sharedPreferenceManager: SharedPreferenceManager, chatAppDao: ChatAppDao): LocalDataSource {
        return LocalDataSource(sharedPreferenceManager = sharedPreferenceManager, chatAppDao = chatAppDao)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): ChatApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ChatApplicationDatabase::class.java,
            "ChatApplication"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideLocalChatAppDao(chatApplicationDatabase: ChatApplicationDatabase): ChatAppDao {
        return chatApplicationDatabase.localDao()
    }
}
