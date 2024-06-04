package com.woosung.invitation.detail

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
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
            ) {
                if (isCardFlipped || rotateCardY > 90) {
                    Surface(
                        modifier = Modifier
                            .size(width = 300.dp, height = 400.dp)
                            .hologramEffect(rotationState),
                    ) {}
                } else {

                    Box(modifier = modifier) {
                        // Glow Shadow
                        // Has quicker offset change and in opposite direction to the Image Card
                        Image(
                            painter = painterResource(id = realImage),
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        x = -(rotationState.roll * 1.5).dp.roundToPx(),
                                        y = (rotationState.pitch * 2).dp.roundToPx()
                                    )
                                }
                                .width(256.dp)
                                .height(356.dp)
                                .align(Alignment.Center)
                                .blur(
                                    radius = 24.dp,
                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                ),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                        )

                        // Edge (used to give depth to card when tilted)
                        // Has slightly slower offset change than Image Card
                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        x = (rotationState.roll * 0.9).dp.roundToPx(),
                                        y = -(rotationState.pitch * 0.9).dp.roundToPx()
                                    )
                                }
                                .width(300.dp)
                                .height(400.dp)
                                .align(Alignment.Center)
                                .background(
                                    color = Color.White.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                        )

                        // Image Card
                        // The image inside has a parallax shift in the opposite direction
                        Image(
                            painter = painterResource(id = realImage),
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        x = rotationState.roll.dp.roundToPx(),
                                        y = -rotationState.pitch.dp.roundToPx()
                                    )
                                }
                                .width(300.dp)
                                .height(400.dp)
                                .align(Alignment.Center)
                                .clip(RoundedCornerShape(16.dp)),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            alignment = BiasAlignment(
                                horizontalBias = (rotationState.roll * 0.005).toFloat(),
                                verticalBias = 0f,
                            )
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
}