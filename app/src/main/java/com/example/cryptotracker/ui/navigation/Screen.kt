package com.example.cryptotracker.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object CoinList
    @Serializable
    data class CoinDetail(val coinId: String)
}