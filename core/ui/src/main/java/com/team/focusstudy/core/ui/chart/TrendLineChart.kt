package com.team.focusstudy.core.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TrendLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF0B5FFF)
) {
    if (values.isEmpty()) return

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val max = values.maxOrNull()?.coerceAtLeast(1f) ?: 1f
        val stepX = if (values.size <= 1) size.width else size.width / (values.size - 1)

        for (i in 0 until values.lastIndex) {
            val start = Offset(
                x = i * stepX,
                y = size.height - (values[i] / max) * size.height
            )
            val end = Offset(
                x = (i + 1) * stepX,
                y = size.height - (values[i + 1] / max) * size.height
            )
            drawLine(color = lineColor, start = start, end = end, strokeWidth = 5f)
        }
    }
}