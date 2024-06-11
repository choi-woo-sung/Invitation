package com.woosung.invitation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.woosung.invitation.detail.ParallaxCardDetail
import com.woosung.invitation.detail.TreeDCardDetail
import kotlin.math.abs


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardDeckActivity() {
    var showDetails by remember { mutableStateOf(true) }
    var selectedCard by remember { mutableStateOf<InvitationCard?>(null) }


    SharedTransitionLayout {
        AnimatedContent(
            targetState = showDetails,
        ) { it ->
            if (it) {
                LazyVerticalGrid(columns = GridCells.Fixed(5)) {
                    itemsIndexed(items = SampleCardDeck.testCard) { idx, it ->
                        NormalCard(
                            isLocked = it.isLocked,
                            frontImage = it.image,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                            onClick = {
                                showDetails = false
                                selectedCard = it
                            },
                            key = it.key
                        )
                    }
                }
            } else {
                selectedCard?.let { selectedCard ->
                    when (selectedCard) {
                        is InvitationCard.NormalCard -> {
                            NormalCardDetail(
                                modifier = Modifier,
                                isLocked = selectedCard.isLocked,
                                frontImage = selectedCard.image,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent,
                                key = selectedCard.key,
                                onClick = {
                                    showDetails = true
                                },
                                onLockClicked = {
                                    selectedCard.isLocked = false
                                }
                            )
                        }

                        is InvitationCard.ThreeDCard -> {
                            TreeDCardDetail(
                                modifier = Modifier,
                                isLocked = selectedCard.isLocked,
                                frontImage = selectedCard.charImage,
                                backGroundImage = selectedCard.backGroundImage,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent,
                                key = selectedCard.key,
                                onClick = {
                                    showDetails = true
                                },
                                onLockClicked = {
                                    selectedCard.isLocked = false
                                }
                            )
                        }

                        is InvitationCard.ParallaxCard -> {
                            ParallaxCardDetail(
                                modifier = Modifier,
                                isLocked = selectedCard.isLocked,
                                realImage = selectedCard.image,
                                frontImage = selectedCard.frontImage,
                                backGroundImage = selectedCard.backGroundImage,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent,
                                key = selectedCard.key,
                                onClick = {
                                    showDetails = true
                                },
                                onLockClicked = {
                                    selectedCard.isLocked = false
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NormalCard(
    modifier: Modifier = Modifier,
    isLocked: Boolean,
    @DrawableRes frontImage: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    onClick: () -> Unit = {}
) {
    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .size(200.dp, 100.dp)
                .sharedElement(
                    rememberSharedContentState(key = key),
                    animatedVisibilityScope = animatedVisibilityScope
                )
        ) {
            if (isLocked) {
                Image(
                    modifier = Modifier.clickable {
                        onClick()
                    }.hologramEffect(null),
                    painter = painterResource(id = R.drawable.img_back),
                    contentDescription = "카드 뒷면",
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Card {

                    Image(
                        painter = painterResource(id = frontImage),
                        modifier = Modifier
                            .height(IntrinsicSize.Max)
                            .clickable {
                                onClick()
                            },
                        contentDescription = "카드 앞면",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}


fun Modifier.hologramEffect(rotationState: RotationState? , isSizeChanged : Boolean= false) = composed {

    val noiseImage = ImageBitmap.imageResource(R.drawable.noise)

    val scale = with(LocalDensity.current) { 65.dp.roundToPx() }
    val logo = ImageBitmap.imageResource(R.drawable.logo).let {
        // Adjusting logo size.
        it.asAndroidBitmap().scale(
            width = it.width * 54 / it.height,
            height = scale
        )
    }.asImageBitmap()

    val rotationXFraction = ((rotationState?.pitch ?: 0f) / 90f)
    val rotationYFraction = ((rotationState?.roll ?: 0f) / 90f)


    val holographicColor = listOf(
        Color(0xFF2AD0CA),
        Color(0xFFE1F664),
        Color(0xFFFEB0FE),
        Color(0xFFABB3FC),
        Color(0xFF5DF7A4),
        Color(0xFF58C4F6),
    )

    val metalColor = listOf(
        Color.White,
        Color.Black,
        Color.White,
        Color.Black,
        Color.White,
        Color.Black,
        Color.White,
        Color.Black,
        Color.White
    )


    val degreeFraction = maxOf(abs(rotationXFraction), abs(rotationYFraction))



    drawWithContent {
        val pivot = Offset(
            x = size.center.x,
            y = size.center.y
        )

        drawRect(
            brush = Brush.radialGradient(
                colorStops = holographicColor.let {
                    arrayOf(
                        (0.1F) to it[0],
                        (0.25F + 0.25F * degreeFraction) to it[1],
                        (0.5F + 0.15F * degreeFraction) to it[2],
                        (0.65F + 0.2F * degreeFraction) to it[3],
                        (0.9F + 0.15F * degreeFraction) to it[4],
                        (1.0f) to it[5],
                    )
                },
                center = Offset(
                    x = (size.width * 0.5F) + (size.width * 0.1F) * -rotationXFraction * rotationXFraction,
                    y = (size.height * 0.5F) + (size.height * 0.5F) * -rotationYFraction * rotationYFraction
                ),
                radius = size.width + size.height * degreeFraction,
                tileMode = TileMode.Mirror
            ),
        )


        val rawSweepFraction = (rotationXFraction + rotationYFraction) * degreeFraction

        val sweepFraction = rawSweepFraction * 0.9F
        Log.d("rawSweepFraction", rawSweepFraction.toString())




//        scale(
//            scaleX = 1.5F,
//            scaleY = 1.5F,
//            pivot = pivot
//        ) {
//            // Rotating sweep gradients to start diagonally.
//            rotate(
//                degrees = 45.0F * rawSweepFraction* 2,
//                pivot = pivot
//            ) {
        drawRect(
            topLeft = Offset.Zero,
            size = size,
            brush = Brush.sweepGradient(
                colorStops = metalColor.let {
                    arrayOf(
                        0.0F to it[0],
                        (0.2F + (0.3F * degreeFraction)) to it[1],
                        (0.4F + (0.1F * degreeFraction)) to it[2],
                        (0.5F + (0.05F * degreeFraction)) to it[3],
                        (0.6F + (0.1F * degreeFraction)) to it[4],
                        (0.7F + (0.26F * degreeFraction)) to it[5],
                        (0.86F + (0.1F * degreeFraction)) to it[6],
                        (0.96F + (0.1F * degreeFraction)) to it[7],
                        1.0F to it[8],
                    )
                },
                center = pivot
            ),
            alpha = 1.0F,
            blendMode = BlendMode.Difference
        )

        drawRect(
            topLeft = Offset.Zero,
            size = size,
            brush = Brush.sweepGradient(
                colorStops = metalColor.let {
                    arrayOf(
                        0.0F to it[0],
                        (0.2F + (0.3F * degreeFraction)) to it[1],
                        (0.4F + (0.1F * degreeFraction)) to it[2],
                        (0.5F + (0.05F * degreeFraction)) to it[3],
                        (0.6F + (0.1F * degreeFraction)) to it[4],
                        (0.7F + (0.26F * degreeFraction)) to it[5],
                        (0.86F + (0.1F * degreeFraction)) to it[6],
                        (0.96F + (0.1F * degreeFraction)) to it[7],
                        1.0F to it[8],
                    )
                },
                center = pivot
            ),
            blendMode = BlendMode.Screen,
            alpha = 1f
        )

        drawRect(
            ShaderBrush(
                shader = ImageShader(
                    noiseImage,
                    tileModeX = TileMode.Repeated,
                    tileModeY =  TileMode.Repeated
                )),
            alpha = 0.5f,
            blendMode = BlendMode.Overlay
        )

//        val x = (center.x - (logo.width / 2f)).roundToInt()
//        val y = (center.y - (logo.height / 2f)).roundToInt()
        val x = (center.x - (logo.width / 2f))
        val y = (center.y - (logo.height / 2f))



        val scaleX = if(isSizeChanged) -3f else 0.5f
        val scaleY = if(isSizeChanged) 2f else 0.5f
        scale(scaleX = scaleX, scaleY = scaleY) {
            drawImage(logo, topLeft = Offset(x , y), blendMode = BlendMode.Difference)
        }

    }
}

@Preview
@Composable
private fun CardDeckActivityPreview() {
    CardDeckActivity()
}


@Stable
sealed class InvitationCard(
    open val key: String,
    open var isLocked: Boolean = false,
    open val image: Int
) {
    data class NormalCard(
        override val key: String,
        override var isLocked: Boolean = false,
        @DrawableRes override val image: Int
    ) : InvitationCard(key, isLocked, image)


    data class ThreeDCard(
        override val key: String,
        override var isLocked: Boolean = false,
        @DrawableRes override val image: Int,
        @DrawableRes val backGroundImage: Int,
        @DrawableRes val charImage: Int
    ) : InvitationCard(key, isLocked, image)

    data class ParallaxCard(
        override val key: String,
        override var isLocked: Boolean = false,
        @DrawableRes override val image: Int,
        @DrawableRes val backGroundImage: Int,
        @DrawableRes val frontImage: Int
    ) : InvitationCard(key, isLocked, image)
}


object SampleCardDeck {
    val testCard = listOf<InvitationCard>(
        InvitationCard.NormalCard(
            key = "1",
            isLocked = true,
            image = R.drawable.img_card1_front,
        ),
        InvitationCard.ThreeDCard(
            key = "2",
            isLocked = false,
            charImage = R.drawable.char_image_1,
            backGroundImage = R.drawable.background_image_1,
            image = R.drawable.real_image_1
        ),
        InvitationCard.NormalCard(
            key = "3",
            isLocked = false,
            image = R.drawable.real_image_2
        ),

        InvitationCard.ParallaxCard(
            key = "4",
            isLocked = false,
            image = R.drawable.real_image_3,
            backGroundImage = R.drawable.background_image_3,
            frontImage = R.drawable.char_image_3
        ),


        )
}