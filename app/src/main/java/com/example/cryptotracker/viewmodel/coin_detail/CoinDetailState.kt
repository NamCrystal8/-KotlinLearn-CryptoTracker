package com.example.cryptotracker.viewmodel.coin_detail

import com.example.cryptotracker.domain.model.CoinDetail
import com.example.cryptotracker.domain.model.CoinPrice

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val chartData: List<CoinPrice> = emptyList(),
    val error: String = ""
)