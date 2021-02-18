package com.raywenderlich.android.librarian.ui.bookReviewDetails.animation

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun animateBookReviewDetails(screenState: BookReviewDetailsScreenState): BookReviewDetailsTransitionState {
  val transition = updateTransition(screenState)

  val imageMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }
  ) { target -> if (target == Loaded) 16.dp else 125.dp }

  val floatingButtonSize by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }
  ) { target -> if (target == Loaded) 56.dp else 0.dp }

  val titleMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }
  ) { target -> if (target == Loaded) 16.dp else 75.dp }

  val contentMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }
  ) { target -> if (target == Loaded) 6.dp else 50.dp }

  val contentAlpha by transition.animateFloat(
    transitionSpec = { tween(durationMillis = 1000) }
  ) { target -> if (target == Loaded) 1f else 0.3f }

  val state = remember { BookReviewDetailsTransitionState() }

  state.apply {
    this.imageMarginTop = imageMarginTop
    this.floatingButtonSize = floatingButtonSize
    this.titleMarginTop = titleMarginTop
    this.contentMarginTop = contentMarginTop
    this.contentAlpha = contentAlpha
  }

  return state
}

class BookReviewDetailsTransitionState {

  var imageMarginTop: Dp by mutableStateOf(0.dp)
  var floatingButtonSize: Dp by mutableStateOf(0.dp)
  var titleMarginTop: Dp by mutableStateOf(0.dp)
  var contentMarginTop: Dp by mutableStateOf(0.dp)

  var contentAlpha: Float by mutableStateOf(0f)
}