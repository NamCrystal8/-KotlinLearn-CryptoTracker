package com.example.cryptotracker.domain.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val price: Double?,
    val change24h: Double?,
    val imageUrl: String
)