package com.woosung.invitation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.woosung.invitation.ui.theme.InvitationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvitationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InvitationTheme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalAnimatableApi::class)
@Composable
fun LookAheadWithSimpleMovableContent() {
    val colors = listOf(
        Color(0xffff6f69),
        Color(0xffffcc5c),
        Color(0xff264653),
        Color(0xFF679138),
    )
    var isInColumn by remember { mutableStateOf(true) }

    val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> = DeferredTargetAnimation(IntOffset.VectorConverter)
    LookaheadScope {


        val items = remember {
            movableContentOf {
                colors.forEach { color ->
                    Box(
                        Modifier
                            .padding(8.dp)
                            .size(80.dp)
                            .background(color, RoundedCornerShape(10))
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isInColumn = !isInColumn },
            contentAlignment = Alignment.Center
        ) {
            if (isInColumn) {
                Column { items() }
            } else {
                Row { items() }
            }
        }
    }
}

@Preview
@Composable
private fun LookAheadWithSimpleMovableContentPreview() {
    Surface {
        LookAheadWithSimpleMovableContent()
    }
}


class ApproachLayoutModifierNode1() : ApproachLayoutModifierNode{
    @OptIn(ExperimentalAnimatableApi::class)
    //placement때 위치를 알수있는 값
    private val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> =
        DeferredTargetAnimation(IntOffset.VectorConverter)

    override fun isMeasurementApproachComplete(lookaheadSize: IntSize): Boolean = true

    override fun Placeable.PlacementScope.isPlacementApproachComplete(lookaheadCoordinates: LayoutCoordinates): Boolean {
        val target: IntOffset = with() {
            lookaheadScopeCoordinates.localLookaheadPositionOf(lookaheadCoordinates).round()
        }
        offsetAnimation.updateTarget(target, coroutineScope)
        return offsetAnimation.isIdle
    }
}
