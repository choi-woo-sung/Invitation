package com.woosung.invitation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardDeckActivity() {
    var showDetails by remember { mutableStateOf(true) }
    var selectedCard by remember { mutableStateOf("") }

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
                                onClick = { key ->
                                    showDetails = false
                                    selectedCard = key
                                },
                                key = idx.toString()
                            )
                        }
                    }
                }
            } else {
                NormalCard(
                    modifier = Modifier,
                    isLocked = false,
                    frontImage = R.drawable.img_card1_front,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    key = selectedCard,
                    onClick = {
                        showDetails = true
                    }
                )
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
    onClick: (key: String) -> Unit = {}
) {
    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .sharedElement(
                    rememberSharedContentState(key = key),
                    animatedVisibilityScope = animatedVisibilityScope
                )
        ) {
            if (isLocked) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.img_back),
                    contentDescription = "카드 뒷면",
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = frontImage),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onClick(key)
                        },
                    contentDescription = "카드 앞면"
                )
            }
        }
    }
    if (isLocked) {
        TextButton(onClick = { /*TODO*/ }) {

        }
    }
}


@Preview
@Composable
private fun CardDeckActivityPreview() {
    CardDeckActivity()
}


sealed class InvitationCard(open val isLocked: Boolean = false, open val image: Int) {
    data class NormalCard(
        override val isLocked: Boolean = false,
        @DrawableRes override val image: Int
    ) : InvitationCard(isLocked, image)
}


object SampleCardDeck {
    val testCard = listOf<InvitationCard>(
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front,
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
        InvitationCard.NormalCard(
            isLocked = false,
            image = R.drawable.img_card1_front
        ),
    )
}