package com.woosung.invitation

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
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    },
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


fun Modifier.hologramEffect(rotationState: RotationState) = composed {


    val sampleHolographicColors = listOf(
        Color(0xFF9FDAFF),
        Color(0xFFFEF1A5),
        Color(0xFFFBA1C9),
        Color(0xFFAB90D3),
        Color(0xFF9FDAFF),
        Color(0xFFFBB466),
    )

    val sampleHolographicMetalColors = listOf(
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

    val degreeFraction = maxOf(abs(rotationState.pitch), abs(rotationState.roll))

    drawWithContent {
        val pivot = Offset(
            x = size.center.x,
            y = size.height * 0.5F
        )

        drawRect(
            brush = Brush.radialGradient(
                colorStops = sampleHolographicColors.let {
                    arrayOf(
                        (0.0F + 0.1F/* * degreeFraction*/) to it[0],
                        (0.2F + 0.1F
                                /** degreeFraction*/
                                ) to it[1],
                        (0.4F + 0.08F
                                /** degreeFraction*/
                                ) to it[2],
                        (0.6F + 0.08F
                                /** degreeFraction*/
                                ) to it[3],
                        (0.8F + 0.06F
                                /** degreeFraction*/
                                ) to it[4],
                        (1.0F + 0.06F
                                /** degreeFraction*/
                                ) to it[5],
                    )
                },
                center = Offset(
                    x = (size.width * 0.5F) + (size.width * 0.25F),
                    y = (size.height * 0.4F) + (size.height * 0.2F)
                ),
                radius = (size.width * 0.75F) + (size.height * 0.6F),
                tileMode = TileMode.Mirror
            ),
        )


        drawRect(
            topLeft = Offset.Zero,
            size = size,
            brush = Brush.sweepGradient(
                colorStops = sampleHolographicMetalColors.let {
                    arrayOf(
                        0.0F to it[0],
                        (0.15F + (0.1F
                                /** sweepFraction*/
                                )) to it[1],
                        (0.25F + (0.08F
                                /** sweepFraction*/
                                )) to it[2],
                        (0.4F + (0.05F
                                /** sweepFraction*/
                                )) to it[3],
                        (0.5F + (0.05F
                                /** sweepFraction*/
                                )) to it[4],
                        (0.55F + (0.03F
                                /** sweepFraction*/
                                )) to it[5],
                        (0.76F + (0.1F
                                /** sweepFraction*/
                                )) to it[6],
                        (0.87F + (0.05F
                                /** sweepFraction*/
                                )) to it[7],
                        1.0F to it[8],
                    )
                },
                center = pivot
            ),
            alpha = 1.0F - (0.25F * (1.0F - degreeFraction))
            /** state.pressFraction)*/
            ,
            blendMode = BlendMode.Difference
        )

        drawRect(
            topLeft = Offset.Zero,
            size = size,
            brush = Brush.sweepGradient(
                colorStops = sampleHolographicMetalColors.let {
                    arrayOf(
                        0.0F to it[0],
                        (0.15F + (0.1F
                                /** sweepFraction*/
                                )) to it[1],
                        (0.25F + (0.08F
                                /** sweepFraction*/
                                )) to it[2],
                        (0.4F + (0.05F
                                /** sweepFraction*/
                                )) to it[3],
                        (0.5F + (0.05F
                                /** sweepFraction*/
                                )) to it[4],
                        (0.55F + (0.03F
                                /** sweepFraction*/
                                )) to it[5],
                        (0.76F + (0.1F
                                /** sweepFraction*/
                                )) to it[6],
                        (0.87F + (0.05F
                                /** sweepFraction*/
                                )) to it[7],
                        1.0F to it[8],
                    )
                },
                center = pivot
            ),
            alpha = 1.0F - (0.25F * (1.0F - degreeFraction))
            /** state.pressFraction)*/
            ,
            blendMode = BlendMode.Screen
        )

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
        @DrawableRes val frontImage: Int
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
            frontImage = R.drawable.char_image_1,
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