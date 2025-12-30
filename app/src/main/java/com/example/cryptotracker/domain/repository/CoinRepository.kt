package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.CoinDetail
import com.example.cryptotracker.domain.model.CoinPrice
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<Resource<List<Coin>>>

    fun getCoinById(coinId: String): Flow<Resource<CoinDetail>>

    fun getCoinMarketChart(coinId: String): Flow<Resource<List<CoinPrice>>>

    fun getCoins(query: String? = null, page: Int = 1): Flow<Resource<List<Coin>>>
}