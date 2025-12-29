package com.example.cryptotracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val description: DescriptionDto,
    val image: ImageDto,
    @SerialName("market_data") val marketData: MarketDataDto
)

@Serializable
data class DescriptionDto(
    val en: String
)

@Serializable
data class ImageDto(
    val large: String
)

@Serializable
data class MarketDataDto(
    @SerialName("current_price") val currentPrice: CurrentPriceDto
)

@Serializable
data class CurrentPriceDto(
    val usd: Double
)