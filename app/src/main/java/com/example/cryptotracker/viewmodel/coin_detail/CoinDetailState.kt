package com.example.cryptotracker.viewmodel.coin_detail

import com.example.cryptotracker.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)