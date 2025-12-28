package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.mapper.toCoin
import com.example.cryptotracker.data.remote.CoinGeckoApi
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi
) : CoinRepository {

    override fun getCoins(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading(true))
        try {
            val remoteDtos = api.getCoins()
            val domainCoins = remoteDtos.map { it.toCoin() }
            emit(Resource.Success(domainCoins))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data. Check internet connection."))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Server error. Code: ${e.code()}"))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}