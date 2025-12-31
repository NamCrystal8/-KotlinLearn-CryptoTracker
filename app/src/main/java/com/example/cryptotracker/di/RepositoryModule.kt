package com.example.cryptotracker.di

import com.example.cryptotracker.data.repository.CryptoRepositoryImpl
import com.example.cryptotracker.domain.repository.CoinRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        cryptoRepositoryImpl: CryptoRepositoryImpl
    ): CoinRepository
}