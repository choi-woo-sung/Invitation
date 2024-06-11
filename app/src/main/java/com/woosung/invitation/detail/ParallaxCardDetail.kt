package com.woosung.invitation.detail

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.woosung.invitation.hologramEffect
import com.woosung.invitation.observeDeviceRotation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ParallaxCardDetail(
    modifier: Modifier = Modifier,
    isLocked: Boolean,
    @DrawableRes frontImage: Int,
    @DrawableRes backGroundImage: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    onClick: (key: String) -> Unit = {},
    onLockClicked: () -> Unit = {},
    @DrawableRes realImage: Int
) {

    var isCardFlipped by remember { mutableStateOf(isLocked) }

    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseInOut),
        label = ""
    )
    val rotationState = observeDeviceRotation()

    val backImageBitmap = ImageBitmap.imageResource(backGroundImage)

    val zAxisDistance = 10f //distance between camera and Card
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(sharedTransitionScope) {
            Box(modifier = modifier
                .sharedElement(
                    rememberSharedContentState(key = key),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .clickable {
                    onClick(key)
                }) {
                if (isCardFlipped || rotateCardY > 90) {
                    Surface(
                        modifier = Modifier
                            .size(width = 300.dp, height = 400.dp)
                            .hologramEffect(rotationState, true),
                    ) {}
                } else {

                    Box(modifier = modifier) {
                        Canvas(
                            modifier
                        ) {
                            drawImage(
                                image = backImageBitmap,
                                srcOffset = IntOffset.Zero,
                                srcSize = IntSize(300, 400)
                            )
                        }
                }
                    }
            }
        }
    }
}