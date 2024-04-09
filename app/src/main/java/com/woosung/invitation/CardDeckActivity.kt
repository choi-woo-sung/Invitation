package com.woosung.invitation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CardDeckActivity() {
    LazyVerticalGrid(columns = GridCells.Fixed(5)) {
        items(items = SampleCardDeck.testCard) {
            when (it) {
                is InvitationCard.NormalCard -> NormalCard(isLocked = it.isLocked, frontImage = it.image)
            }
        }
    }
}


@Composable
fun NormalCard(modifier: Modifier = Modifier, isLocked: Boolean, @DrawableRes frontImage: Int) {
    Box() {
        if (isLocked) {
            Image(painter = painterResource(id = R.drawable.img_back), contentDescription = "카드 뒷면")
        } else {
            Image(painter = painterResource(id = frontImage), contentDescription = "카드 앞면")
        }
    }
}


@Preview
@Composable
private fun CardDeckActivityPreview() {
    CardDeckActivity()
}


sealed class InvitationCard(open val isLocked: Boolean = false, open val image: Int) {
    data class NormalCard(override val isLocked: Boolean = false, @DrawableRes override val image: Int) : InvitationCard(isLocked, image)
}


object SampleCardDeck {
    val testCard = listOf<InvitationCard>(InvitationCard.NormalCard(isLocked = false, image = R.drawable.img_card1_front))
}