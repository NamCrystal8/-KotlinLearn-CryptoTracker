package com.example.cryptotracker.viewmodel.coin_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.use_case.GetCoinUseCase
import com.example.cryptotracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CoinListState())
    val state: StateFlow<CoinListState> = _state.asStateFlow()

    init {
        getCoins()
    }

    fun onRefresh() {
        getCoins()
    }

    private fun getCoins() {
        getCoinUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val coins = result.data ?: emptyList()
                    Log.d("ViewModel", "✅ Success! Loaded ${coins.size} coins.")
                    _state.value = CoinListState(
                        coins = coins,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    Log.e("ViewModel", "❌ Error: ${result.message}")
                    _state.value = CoinListState(
                        error = result.message ?: "Unknown Error",
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = result.isLoading
                    )
                    Log.d("ViewModel", "Loading state updated to: ${result.isLoading}")
                }
            }
        }.launchIn(viewModelScope)
    }


}