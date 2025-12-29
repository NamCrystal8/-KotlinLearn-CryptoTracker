package com.example.cryptotracker.ui.coin_detail_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.domain.model.CoinPrice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun LineChart(
    data: List<CoinPrice>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val startPrice = data.first().price
    val endPrice = data.last().price
    val isProfit = endPrice >= startPrice
    val chartColor = if (isProfit) Color.Green else Color.Red

    val minPrice = remember(data) { data.minOf { it.price } }
    val maxPrice = remember(data) { data.maxOf { it.price } }
    val priceRange = maxPrice - minPrice

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val index = (offset.x / size.width * (data.size - 1)).roundToInt()
                        selectedIndex = index.coerceIn(0, data.lastIndex)
                        tryAwaitRelease()
                        selectedIndex = null
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        val index = (offset.x / size.width * (data.size - 1)).roundToInt()
                        selectedIndex = index.coerceIn(0, data.lastIndex)
                    },
                    onDragEnd = { selectedIndex = null },
                    onDragCancel = { selectedIndex = null },
                    onHorizontalDrag = { change, _ ->
                        val index = (change.position.x / size.width * (data.size - 1)).roundToInt()
                        selectedIndex = index.coerceIn(0, data.lastIndex)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val spacePerPoint = width / (data.size - 1)

            val strokePath = Path().apply {
                data.forEachIndexed { index, coinPrice ->
                    val x = index * spacePerPoint
                    val priceRatio = (coinPrice.price - minPrice) / priceRange
                    val y = height - (priceRatio * height).toFloat()
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(chartColor.copy(alpha = 0.5f), Color.Transparent),
                    endY = height
                )
            )

            drawPath(
                path = strokePath,
                color = chartColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            selectedIndex?.let { index ->
                val x = index * spacePerPoint
                val coinPrice = data[index]
                val priceRatio = (coinPrice.price - minPrice) / priceRange
                val y = height - (priceRatio * height).toFloat()

                drawLine(
                    color = Color.White.copy(alpha = 0.7f),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 2f
                )

                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )
                drawCircle(
                    color = chartColor,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        selectedIndex?.let { index ->
            val coinPrice = data[index]

            val date = Date(coinPrice.timestamp)
            val formattedDate = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            ChartTooltip(
                price = coinPrice.price,
                time = formattedDate,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ChartTooltip(price: Double, time: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(top = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$$price",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}