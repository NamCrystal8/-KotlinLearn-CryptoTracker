package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.model.CryptoDto
import com.example.cryptotracker.domain.model.Coin

fun CryptoDto.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        price = currentPrice,
        change24h = priceChangePercentage24h,
        imageUrl = imageUrl
    )
}