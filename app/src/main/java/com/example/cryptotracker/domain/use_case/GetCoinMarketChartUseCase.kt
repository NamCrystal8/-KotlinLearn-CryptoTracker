package com.example.cryptotracker.domain.use_case

import com.example.cryptotracker.domain.model.CoinPrice
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinMarketChartUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<List<CoinPrice>>> {
        return repository.getCoinMarketChart(coinId)
    }
}