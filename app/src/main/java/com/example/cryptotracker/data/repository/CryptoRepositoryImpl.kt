package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.local.CoinDao
import com.example.cryptotracker.data.mapper.toCoin
import com.example.cryptotracker.data.mapper.toCoinDetail
import com.example.cryptotracker.data.mapper.toCoinEntity
import com.example.cryptotracker.data.mapper.toCoinPrices
import com.example.cryptotracker.data.remote.CoinGeckoApi
import com.example.cryptotracker.utils.safeApiCall
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.CoinDetail
import com.example.cryptotracker.domain.model.CoinPrice
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val dao: CoinDao
) : CoinRepository {
    override fun getCoins(query: String?, page: Int): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading(true))

        if (page == 1) {
            val localCoins = if (!query.isNullOrBlank()) {
                dao.searchCoins(query).map { it.toCoin() }
            } else {
                dao.getCoins().map { it.toCoin() }
            }
            if (localCoins.isNotEmpty()) {
                emit(Resource.Success(localCoins))
            }
        }
        try {
            val remoteCoinsEntities = if (!query.isNullOrBlank()) {
                val searchResult = api.searchCoins(query)
                if (searchResult.coins.isEmpty()) emptyList()
                else {
                    val ids = searchResult.coins.take(10).joinToString(",") { it.id }
                    api.getCoins(ids = ids).map { it.toCoinEntity() }
                }
            } else {
                api.getCoins(page = page).map { it.toCoinEntity() }
            }
            if (page == 1 && query.isNullOrBlank() && remoteCoinsEntities.isNotEmpty()) {
                dao.clearCoins()
            }
            dao.insertCoins(remoteCoinsEntities)
            val newCoins = if (page == 1 && query.isNullOrBlank()) {
                dao.getCoins().map { it.toCoin() }
            } else {
                remoteCoinsEntities.map { it.toCoin() }
            }

            emit(Resource.Success(newCoins))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Showing cached data."))
        } catch (e: HttpException) {
            emit(Resource.Error("Server error occurred."))
        }

        emit(Resource.Loading(false))
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
}