package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.model.MarketChartDto
import com.example.cryptotracker.domain.model.CoinPrice

fun MarketChartDto.toCoinPrices(): List<CoinPrice> {
    return prices.map { item ->
        CoinPrice(
            timestamp = item[0].toLong(),
            price = item[1]
        )
    }
}