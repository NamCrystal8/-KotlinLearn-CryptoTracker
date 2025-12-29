package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptotracker.ui.coin_detail_screen.CoinDetailScreen
import com.example.cryptotracker.ui.coin_screen.CoinListScreen
import com.example.cryptotracker.ui.navigation.Screen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CryptoTrackerTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CoinList
                    ) {
                        composable<Screen.CoinList> {
                            CoinListScreen(
                                onCoinClick = { coin ->
                                    print("here")
                                    navController.navigate(Screen.CoinDetail(coin.id))
                                }
                            )
                        }
                        composable<Screen.CoinDetail> {
                            CoinDetailScreen(
                                onClickBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}