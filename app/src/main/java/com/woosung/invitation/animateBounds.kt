//package com.woosung.invitation
//
//import androidx.compose.animation.core.DeferredTargetAnimation
//import androidx.compose.animation.core.ExperimentalAnimatableApi
//import androidx.compose.animation.core.VectorConverter
//import androidx.compose.animation.core.tween
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.layout.LookaheadScope
//import androidx.compose.ui.layout.approachLayout
//import androidx.compose.ui.unit.Constraints
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.unit.round
//
//
////context reciver는 무엇일까?
////아 확장함수 할때 장점이 있긴 하것네.. 둘다 확장이 필요한데 확장함수는 하나만 만들수 있으니까
//context(LookaheadScope)
//@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimatableApi::class)
//fun Modifier.animateBounds(): Modifier = composed {
//    val offsetAnim = remember { DeferredTargetAnimation(IntOffset.VectorConverter) }
//    val sizeAnim = remember { DeferredTargetAnimation(IntSize.VectorConverter) }
//    val scope = rememberCoroutineScope()
//    this.approachLayout(
//        isMeasurementApproachComplete = {
//            sizeAnim.updateTarget(it, scope, tween(2000))
//            sizeAnim.isIdle
//        },
//        isPlacementApproachComplete = {
//            val target = lookaheadScopeCoordinates.localLookaheadPositionOf(it)
//            offsetAnim.updateTarget(target.round(), scope , tween(2000))
//            offsetAnim.isIdle
//        },
//    ) { measurable, constraints ->
//        val (animWidth, animHeight) = sizeAnim.updateTarget(lookaheadSize, scope)
//        measurable.measure(Constraints.fixed(animWidth, animHeight)).run {
//            layout(width, height) {
//                coordinates?.let {
//                    val target = lookaheadScopeCoordinates.localLookaheadPositionOf(it).round()
//                    val animOffset = offsetAnim.updateTarget(target, scope)
//                    val current = lookaheadScopeCoordinates.localPositionOf(it, Offset.Zero).round()
//                    val (x, y) = animOffset - current
//                    place(x, y)
//                } ?: place(0, 0)
//            }
//        }
//    }
//}