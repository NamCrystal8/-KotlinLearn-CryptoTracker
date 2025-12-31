package com.example.cryptotracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    val coins: List<SearchCoinDto>
)

@Serializable
data class SearchCoinDto(
    val id: String,
    val name: String,
    val symbol: String,
    val thumb: String
)