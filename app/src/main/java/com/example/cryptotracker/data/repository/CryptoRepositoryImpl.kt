package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.mapper.toCoin
import com.example.cryptotracker.data.mapper.toCoinDetail
import com.example.cryptotracker.data.remote.CoinGeckoApi
import com.example.cryptotracker.data.utils.safeApiCall
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.CoinDetail
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

    override fun getCoinById(coinId: String): Flow<Resource<CoinDetail>> {
        return safeApiCall(
            apiCall = { api.getCoinDetail(coinId) },
            mapper = { dto -> dto.toCoinDetail() }
        )
    }
}