package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.mapper.toCoin
import com.example.cryptotracker.data.mapper.toCoinDetail
import com.example.cryptotracker.data.mapper.toCoinPrices
import com.example.cryptotracker.data.remote.CoinGeckoApi
import com.example.cryptotracker.data.utils.safeApiCall
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.CoinDetail
import com.example.cryptotracker.domain.model.CoinPrice
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

    override fun getCoinMarketChart(coinId: String): Flow<Resource<List<CoinPrice>>> {
        return safeApiCall(
            apiCall = { api.getMarketChart(coinId) },
            mapper = { dto -> dto.toCoinPrices() }
        )
    }

    override fun getCoins(query: String?, page: Int): Flow<Resource<List<Coin>>> {
        return safeApiCall(
            apiCall = {
                if (query.isNullOrBlank()) {
                    api.getCoins(page = page)
                } else {
                    val searchResult = api.searchCoins(query)

                    if (searchResult.coins.isEmpty()) {
                        emptyList()
                    } else {
                        val commaSeparatedIds = searchResult.coins
                            .take(10) // Limit to top 10 matches
                            .joinToString(",") { it.id }
                        api.getCoins(ids = commaSeparatedIds)
                    }
                }
            },
            mapper = { dtos -> dtos.map { it.toCoin() } }
        )
    }
}