package com.example.cryptotracker.di

import android.app.Application
import androidx.room.Room
import com.example.cryptotracker.data.local.CoinDao
import com.example.cryptotracker.data.local.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoinDatabase(app: Application): CoinDatabase {
        return Room.databaseBuilder(
            app,
            CoinDatabase::class.java,
            "coin_db.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(db: CoinDatabase): CoinDao {
        return db.coinDao
    }
}