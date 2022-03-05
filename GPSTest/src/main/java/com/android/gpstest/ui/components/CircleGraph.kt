/*
 * Copyright (C) 2021 Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.gpstest.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.android.gpstest.R
import com.android.gpstest.ui.dashboard.helpIconAlpha
import com.android.gpstest.ui.dashboard.subtitleStyle
import com.android.gpstest.ui.dashboard.titleStyle

/**
 * A circular graph showing a percentage full (e.g., # of sats used / in view, # of signals used / in view),
 * with a text value ([number]/[maxNumber]), an icon based on the [iconId], and [descriptionText]
 * below.
 */
@Composable
fun CircleGraph(
    number: Int,
    maxNumber: Int,
    inactiveBarColor: Color = MaterialTheme.colors.onBackground.copy(alpha = helpIconAlpha),
    activeBarBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.primaryVariant
        )
    ),
    strokeWidth: Dp = 12.dp,
    @DrawableRes iconId: Int,
    iconSize: Dp = 40.dp,
    descriptionText: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(5.dp)
            .size(130.dp)
    ) {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        var value by remember { mutableStateOf(0.0f) }
        value = number.toFloat() / maxNumber.toFloat()

        val animatedValue = animateFloatAsState(
            targetValue = value,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(5.dp)
                .size(100.dp)
                .onSizeChanged {
                    size = it
                }
        ) {
            Canvas(
                Modifier
                    .fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                drawArc(
                    color = inactiveBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f,
                    useCenter = false,
                    size = Size(width.toFloat(), height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    brush = activeBarBrush,
                    startAngle = -215f,
                    sweepAngle = 250f * animatedValue,
                    useCenter = false,
                    size = Size(width.toFloat(), height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    iconId
                ),
                contentDescription = stringResource(R.string.mode_satellite),
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .size(iconSize),
                tint = MaterialTheme.colors.onBackground.copy(alpha = helpIconAlpha),
            )
            Text(
                text = "$number/$maxNumber",
                style = titleStyle.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = descriptionText,
                style = subtitleStyle
            )
        }
    }
}