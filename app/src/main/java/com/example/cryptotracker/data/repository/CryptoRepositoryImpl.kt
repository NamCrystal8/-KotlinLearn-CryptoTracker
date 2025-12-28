package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.mapper.toCoin
import com.example.cryptotracker.data.remote.CoinGeckoApi
import com.example.cryptotracker.data.utils.safeApiCall // Import your helper
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi
) : CoinRepository {

    override fun getCoins(): Flow<Resource<List<Coin>>> {
        return safeApiCall(
            apiCall = { api.getCoins() },
            mapper = { dtos -> dtos.map { it.toCoin() } }
        )
    }
}