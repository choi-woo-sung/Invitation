package com.woosung.invitation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
                        when (it) {
                            is InvitationCard.NormalCard -> NormalCard(
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
                }
            } else {
                selectedCard?.let { selectedCard ->
                    CardDetail(
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
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(id = frontImage),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onClick()
                        },
                    contentDescription = "카드 앞면"
                )
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardDetail(
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
    val zAxisDistance = 10f //distance between camera and Card



    Column {
        with(sharedTransitionScope) {
            Column(
                modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = key),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clickable {
                        onClick(key)
                    }.graphicsLayer {
                    rotationY = rotateCardY
//                    cameraDistance = zAxisDistance
                }
            ) {
                if (isCardFlipped || rotateCardY > 90) {
                    Image(
                        modifier = Modifier.size(width = 200.dp , height = 300.dp),
                        painter = painterResource(id = R.drawable.img_back),
                        contentDescription = "카드 뒷면",
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = frontImage),
                        modifier = Modifier.size(width = 200.dp , height = 300.dp),
                        contentDescription = "카드 앞면",
                        contentScale = ContentScale.Crop
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
}


object SampleCardDeck {
    val testCard = listOf<InvitationCard>(
        InvitationCard.NormalCard(
            key = "1",
            isLocked = true,
            image = R.drawable.img_card1_front,
        ),
        InvitationCard.NormalCard(
            key = "2",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "3",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "4",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "5",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "6",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "7",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "8",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "9",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "10",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            key = "11",
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
    )
}