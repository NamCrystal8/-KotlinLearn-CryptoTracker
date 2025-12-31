package com.example.cryptotracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)
    @Query("DELETE FROM coin_table")
    suspend fun clearCoins()

    @Query("""
        SELECT * FROM coin_table
        WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%'
    """)
    suspend fun searchCoins(query: String): List<CoinEntity>

    @Query("SELECT * FROM coin_table")
    suspend fun getCoins(): List<CoinEntity>
}