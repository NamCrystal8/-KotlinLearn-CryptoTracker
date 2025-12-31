package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.local.CoinEntity
import com.example.cryptotracker.data.model.CryptoDto
import com.example.cryptotracker.domain.model.Coin

// 1. API -> Database
fun CryptoDto.toCoinEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        name = name,
        symbol = symbol,
        price = currentPrice,
        imageUrl = imageUrl,
        change24h = priceChangePercentage24h
    )
}

fun CoinEntity.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        price = price,
        imageUrl = imageUrl,
        change24h = change24h
    )
}