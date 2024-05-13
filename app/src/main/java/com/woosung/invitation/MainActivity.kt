//package com.woosung.invitation
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.animation.core.AnimationVector2D
//import androidx.compose.animation.core.DeferredTargetAnimation
//import androidx.compose.animation.core.ExperimentalAnimatableApi
//import androidx.compose.animation.core.VectorConverter
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.movableContentOf
//import androidx.compose.runtime.movableContentWithReceiverOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ApproachLayoutModifierNode
//import androidx.compose.ui.layout.ApproachMeasureScope
//import androidx.compose.ui.layout.LayoutCoordinates
//import androidx.compose.ui.layout.LookaheadScope
//import androidx.compose.ui.layout.Measurable
//import androidx.compose.ui.layout.MeasureResult
//import androidx.compose.ui.layout.Placeable
//import androidx.compose.ui.node.ModifierNodeElement
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Constraints
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.round
//import com.woosung.invitation.ui.theme.InvitationTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            InvitationTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    InvitationTheme {
//        Greeting("Android")
//    }
//}
//
//@Composable
//fun LookAheadWithSimpleMovableContent() {
//    val colors = listOf(
//        Color(0xffff6f69),
//        Color(0xffffcc5c),
//        Color(0xff264653),
//        Color(0xFF679138),
//    )
//    var isInColumn by remember { mutableStateOf(true) }
//    //룩어헤드스코프 인터페이스와 룩어헤드스코프 컴포저블이 있습니다.
//    // 이 인터페이스는 컴포저블 내의 모든 자식 레이아웃에 대한 리시버 스코프입니다.
//    // 이 인터페이스는 모든 자손의 PlacementScope에서 lookaheadScopeCoordinates에 대한 액세스를 제공합니다.
//    // 이를 통해 모든 자식은 toLookaheadCoordinates() 함수를 사용하여 레이아웃 좌표 공간의 레이아웃 좌표를 룩어헤드 좌표로 변환할 수 있습니다.
//
//    LookaheadScope {
//
//        val items = remember {
//            movableContentWithReceiverOf<LookaheadScope> {
//                colors.forEach { color ->
//                    Box(
//                        Modifier
//                            .padding(8.dp)
//                            .size(if (isInColumn) 80.dp else 20.dp)
////                            .animatePlacementInScope(this)
//                            .animateBounds()
//                            .background(color, RoundedCornerShape(10))
//                    )
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .clickable { isInColumn = !isInColumn },
//            contentAlignment = Alignment.Center
//        ) {
//            if (isInColumn) {
//                Column { items() }
//            } else {
//                Row { items() }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun LookAheadWithSimpleMovableContentPreview() {
//    Surface {
//        LookAheadWithSimpleMovableContent()
//    }
//}
//
//data class AnimatePlacementNodeElement(val lookaheadScope: LookaheadScope) :
//    ModifierNodeElement<CustomApproachLayoutModifierNode>() {
//
//    override fun update(node: CustomApproachLayoutModifierNode) {
//        node.lookaheadScope = lookaheadScope
//    }
//
//    override fun create(): CustomApproachLayoutModifierNode {
//        return CustomApproachLayoutModifierNode(lookaheadScope)
//    }
//}
//
//
//class CustomApproachLayoutModifierNode(
//    var lookaheadScope: LookaheadScope
//) : ApproachLayoutModifierNode, Modifier.Node() {
//
//
//    //measure and placement phase 에서 크기를 알수 없는 애니메이션에 사용될 수 있다.
//    // updateTarget은 지정된 코루틴 범위에서 애니메이션을 실행한 후, 애니메이션의 현재값을 반환한다.
//    @OptIn(ExperimentalAnimatableApi::class)
//    private val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> = DeferredTargetAnimation(IntOffset.VectorConverter)
//
//    override fun isMeasurementApproachComplete(lookaheadSize: IntSize): Boolean = true
//
//
//    // 오프셋 애니메이션이 완료 되면 참을 반환하고 아니면 거짓을 반환한다.
//    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimatableApi::class)
//    override fun Placeable.PlacementScope.isPlacementApproachComplete(lookaheadCoordinates: LayoutCoordinates): Boolean {
//        //레이아웃의 목표위치를 알수 있다.
//        val target: IntOffset = with(lookaheadScope) {
//            //룩어헤드 좌표 공간에서 레이아웃의 로컬 위치를 계산
//            lookaheadScopeCoordinates.localLookaheadPositionOf(lookaheadCoordinates).round()
//        }
//        //
//        offsetAnimation.updateTarget(target, coroutineScope)
//        return offsetAnimation.isIdle
//    }
//
//
//    @OptIn(ExperimentalAnimatableApi::class)
//    @ExperimentalComposeUiApi
//    override fun ApproachMeasureScope.approachMeasure(measurable: Measurable, constraints: Constraints): MeasureResult {
//        val placeable = measurable.measure(constraints)
//        return layout(placeable.width, placeable.height) {
//            val coordinates = coordinates
//            if (coordinates != null) {
//                //예측값
//                val target = with(lookaheadScope) {
//                    lookaheadScopeCoordinates.localLookaheadPositionOf(coordinates).round()
//                }
//                val animatedOffset = offsetAnimation.updateTarget(target, coroutineScope)
//
//                val placementOffset = with(lookaheadScope) {
//                    lookaheadScopeCoordinates.localPositionOf(coordinates, Offset.Zero).round()
//                }
//                val (x, y) = animatedOffset - placementOffset
//                placeable.place(x, y)
//            } else {
//                placeable.place(0, 0)
//            }
//        }
//    }
//}
//
//fun Modifier.animatePlacementInScope(lookaheadScope: LookaheadScope): Modifier {
//    return this.then(AnimatePlacementNodeElement(lookaheadScope))
//}
//
//
