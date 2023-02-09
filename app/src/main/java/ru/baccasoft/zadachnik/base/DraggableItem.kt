package ru.baccasoft.zadachnik.base

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.features.mainScreen.TaskModel
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 500
const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableCard(
    card: TaskModel,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: (TaskModel) -> Unit,
    onCollapse: (TaskModel) -> Unit,
    onClick: () -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val cardBgColor by transition.animateColor(
        label = "cardBgColorTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = {
            if (isRevealed) colorResource(id = R.color.white) else colorResource(id = R.color.white)
        }
    )

    //сдвиг карточки (в коде 100.dp)
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset else 0f },

        )

    //тень/объем
    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 40.dp else 2.dp }
    )

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(card) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onExpand(card)
                        dragAmount < -MIN_DRAG_AMOUNT -> onCollapse(card)
                    }
                }
            },
        backgroundColor = cardBgColor,
        shape = remember {
            RoundedCornerShape(0.dp)
        },
        elevation = cardElevation,
        content = {
            TaskCell(
                modifier = Modifier.fillMaxWidth(),
                task = card,
                onClick =
                (if (offsetTransition == 0f) {
                    {
                        onClick()
                    }
                } else {
                    {}
                })
            )
        }
    )
}