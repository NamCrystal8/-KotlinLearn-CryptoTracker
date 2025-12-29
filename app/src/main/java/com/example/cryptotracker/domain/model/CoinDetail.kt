package com.example.cryptotracker.domain.model

data class CoinDetail(
    val coinId: String,
    val name: String,
    val description: String,
    val symbol: String,
    val price: Double,
    val imageUrl: String
)