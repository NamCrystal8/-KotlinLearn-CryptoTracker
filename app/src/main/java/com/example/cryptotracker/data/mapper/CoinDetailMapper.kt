package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.model.CoinDetailDto
import com.example.cryptotracker.domain.model.CoinDetail

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        coinId = id,
        name = name,
        description = description.en,
        symbol = symbol,
        price = marketData.currentPrice.usd,
        imageUrl = image.large
    )
}