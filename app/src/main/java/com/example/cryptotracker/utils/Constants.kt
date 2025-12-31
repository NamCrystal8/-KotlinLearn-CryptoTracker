package com.example.cryptotracker.utils

/**
 * Application-wide constants to avoid magic numbers and strings
 */
object Constants {
    // Network
    const val BASE_URL = "https://api.coingecko.com/api/v3/"
    
    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val FIRST_PAGE = 1
    
    // Search
    const val SEARCH_DEBOUNCE_MS = 500L
    const val MAX_SEARCH_RESULTS = 10
    
    // Chart
    const val CHART_DAYS_DEFAULT = 1
}
