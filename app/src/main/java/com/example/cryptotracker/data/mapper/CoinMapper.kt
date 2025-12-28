package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.model.CryptoDto
import com.example.cryptotracker.domain.model.Coin

fun CryptoDto.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        imageUrl = imageUrl,
        // If API sends null, default to 0.0
        price = currentPrice ?: 0.0,
        change24h = priceChangePercentage24h ?: 0.0
    )
}