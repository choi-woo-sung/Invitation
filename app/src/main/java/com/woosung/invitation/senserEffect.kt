package com.woosung.invitation

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import kotlin.math.abs


@SuppressLint("RestrictedApi")
fun Modifier.rotationEffect() = composed {

    val rotationState = observeDeviceRotation()


    this.graphicsLayer {
        this.rotationX = rotationState.pitch
        this.rotationY = rotationState.roll
    }

}