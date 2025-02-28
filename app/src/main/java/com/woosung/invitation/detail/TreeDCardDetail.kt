package com.woosung.invitation.detail

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.woosung.invitation.R
import com.woosung.invitation.getTranslation
import com.woosung.invitation.hologramEffect
import com.woosung.invitation.observeDeviceRotation

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

    val noiseImage = ImageBitmap.imageResource(R.drawable.noise)


    val rotationState = observeDeviceRotation()


    val zAxisDistance = 10f //distance between camera and Card
    Column(
        modifier.fillMaxSize().drawWithContent {
            drawContent()
        },
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
                            .hologramEffect(rotationState, true),
                    ) {}
                } else {
                    Card(
                        modifier = Modifier
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(0.5f, 0.5f)
                                cameraDistance = 16.dp.value
                                this.rotationX = rotationState.pitch
                                this.rotationY = rotationState.roll
                            }
                            .align(Alignment.Center),
                        elevation = CardDefaults.elevatedCardElevation(0.dp),
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = backGroundImage),
                                modifier = Modifier.size(width = 300.dp, height = 400.dp)
                                    .drawWithContent {
                                    drawContent()
                                        drawRect(
                                            ShaderBrush(
                                                shader = ImageShader(
                                                    noiseImage,
                                                    tileModeX = TileMode.Repeated,
                                                    tileModeY =  TileMode.Repeated
                                                )
                                            ),
                                            alpha = 1f,
                                            blendMode = BlendMode.Overlay
                                        )
                                },
                                contentDescription = "카드 앞면",
                                contentScale = ContentScale.Crop
                            )



                            Image(
                                painter = painterResource(id = frontImage),
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            x = -(rotationState.roll * 1.5).dp.roundToPx(),
                                            y = (rotationState.pitch * 2).dp.roundToPx()
                                        )
                                    }
                                    .size(width = 250.dp, height = 250.dp)
                                    .align(Alignment.BottomCenter)
                                    .blur(
                                        radius = 24.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    ),
                                contentDescription = "그림자",
                                contentScale = ContentScale.Fit
                            )
                        }
                    }


                    Image(
                        painter = painterResource(id = frontImage),
                        modifier = Modifier
                            .graphicsLayer {
                                this.rotationX = rotationState.pitch
                                this.rotationY = rotationState.roll
                            }
                            .offset {
                                IntOffset(
                                    x = rotationState.roll.dp.roundToPx(),
                                    y = -rotationState.pitch.dp.roundToPx()
                                )
                            }
                            .width(250.dp)
                            .height(300.dp)
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                    )

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