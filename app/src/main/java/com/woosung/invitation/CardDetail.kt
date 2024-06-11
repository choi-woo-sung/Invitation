package com.woosung.invitation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

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
                    Box(
                        modifier = Modifier
                            .size(width = 300.dp, height = 500.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .hologramEffect(rotationState, true),
                    ) {}
                } else {
                    Card {
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
    return ((angle / 90f) * maxDistance).also {
        Log.d("transition", "$angle , result = $it")
    }
}