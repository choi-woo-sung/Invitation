package com.woosung.invitation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NormalCardDetail(
    modifier: Modifier = Modifier,
    isLocked: Boolean,
    @DrawableRes frontImage: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    onClick: (key: String) -> Unit = {},
    onLockClicked: () -> Unit = {}
) {

    var isCardFlipped by remember { mutableStateOf(isLocked) }

    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseInOut),
        label = ""
    )
    val rotationState = observeDeviceRotation()


    val zAxisDistance = 10f //distance between camera and Card
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(sharedTransitionScope) {
            Column(
                modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = key),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clickable {
                        onClick(key)
                    }
                    .graphicsLayer {
                        rotationY = rotateCardY
//                    cameraDistance = zAxisDistance
                    }
                    .graphicsLayer {
                        this.rotationX = rotationState.pitch
                        this.rotationY = rotationState.roll
                    }
            ) {
                if (isCardFlipped || rotateCardY > 90) {
                    Surface(
                        modifier = Modifier
                            .size(width = 300.dp, height = 500.dp)
                            .hologramEffect(rotationState),
                    ) {}
                } else {
                    Image(
                        painter = painterResource(id = frontImage),
                        modifier = Modifier.size(width = 300.dp, height = 500.dp),
                        contentDescription = "카드 앞면",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
        if (isLocked) {
            TextButton(onClick = {
                isCardFlipped = false
            }) {
                Text("카드를 열어보려면 클릭하세요")
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TreeDCardDetail(
    modifier: Modifier = Modifier,
    isLocked: Boolean,
    @DrawableRes frontImage: Int,
    @DrawableRes backGroundImage: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    onClick: (key: String) -> Unit = {},
    onLockClicked: () -> Unit = {}
) {

    var isCardFlipped by remember { mutableStateOf(isLocked) }

    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseInOut),
        label = ""
    )
    val rotationState = observeDeviceRotation()


    val zAxisDistance = 10f //distance between camera and Card
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(sharedTransitionScope) {
            Box(
                modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = key),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clickable {
                        onClick(key)
                    }
                    .graphicsLayer {
                        rotationY = rotateCardY
//                    cameraDistance = zAxisDistance
                    }
                    .graphicsLayer {
                        this.rotationX = rotationState.pitch
                        this.rotationY = rotationState.roll
                    }
            ) {
                if (isCardFlipped || rotateCardY > 90) {
                    Surface(
                        modifier = Modifier
                            .size(width = 300.dp, height = 500.dp)
                            .hologramEffect(rotationState),
                    ) {}
                } else {
                    Card (
                        modifier= Modifier.graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                            cameraDistance = 16.dp.value
                        }.align(Alignment.Center),
                        elevation = CardDefaults.elevatedCardElevation(0.dp),
                    ) {
                        Image(
                            painter = painterResource(id = backGroundImage),
                            modifier = Modifier.size(width = 300.dp, height = 500.dp),
                            contentDescription = "카드 앞면",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(0.5f, 0.5f)
                                cameraDistance = 16.dp.value
                                translationX = -getTranslation(rotationState.pitch, 140.dp.value)
                                translationY = -getTranslation(rotationState.roll, 140.dp.value)
                            }
                            .align(Alignment.Center),
//                        elevation = CardDefaults.elevatedCardElevation(0.dp),
                    ) {
                        Image(
                            painter = painterResource(id = frontImage),
                            modifier = Modifier.size(width = 300.dp, height = 500.dp),
                            contentDescription = "카드 앞면",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        if (isLocked) {
            TextButton(onClick = {
                isCardFlipped = false
            }) {
                Text("카드를 열어보려면 클릭하세요")
            }
        }
    }
}


fun getTranslation(angle: Float, maxDistance: Float): Float {
    return (angle/90f) * maxDistance
}
