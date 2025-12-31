package com.example.cryptotracker.viewmodel.coin_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.use_case.GetCoinDetailUseCase
import com.example.cryptotracker.domain.use_case.GetCoinMarketChartUseCase
import com.example.cryptotracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinMarketChartUseCase: GetCoinMarketChartUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(CoinDetailState())
    val state: StateFlow<CoinDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("coinId")?.let { coinId ->
            getCoinDetail(coinId)
        }
    }

    private fun getCoinDetail(coinId: String) {
        getCoinDetailUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        coin = result.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "Unknown Error",
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = result.isLoading
                    )
                }
            }
        }.launchIn(viewModelScope)
        getCoinMarketChartUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        chartData = result.data ?: emptyList(),
                        chartError = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        chartError = result.message ?: "Failed to load chart data"
                    )
                }

                is Resource.Loading -> {
                    // Chart loading is secondary, don't affect main loading state
                }
            }
        }.launchIn(viewModelScope)
    }
}