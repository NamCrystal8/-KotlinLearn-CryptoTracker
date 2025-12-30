package com.example.cryptotracker.viewmodel.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.use_case.GetCoinUseCase
import com.example.cryptotracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state: StateFlow<CoinListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    private var currentQuery: String? = null

    init {
        getCoins()
    }

    fun onRefresh() {
        _state.value = CoinListState(isLoading = true)
        currentQuery = null
        getCoins()
    }

    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            currentQuery = query
            _state.value = CoinListState(isLoading = true)
            getCoins(query = query)
        }
    }

    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.endReached) return

        getCoins(query = currentQuery, isPagination = true)
    }

    private fun getCoins(query: String? = null, isPagination: Boolean = false) {
        val currentPage = if (isPagination) _state.value.page else 1
        getCoinUseCase(query = query, page = currentPage).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newCoins = result.data ?: emptyList()
                    val oldCoins = if (isPagination) _state.value.coins else emptyList()

                    _state.value = _state.value.copy(
                        coins = oldCoins + newCoins,
                        isLoading = false,
                        page = if (newCoins.isNotEmpty()) currentPage + 1 else currentPage,
                        endReached = newCoins.isEmpty()
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
    }
}