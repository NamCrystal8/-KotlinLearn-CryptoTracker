package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<Resource<List<Coin>>>
}