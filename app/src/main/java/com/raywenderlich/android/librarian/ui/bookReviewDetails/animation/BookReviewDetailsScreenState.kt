package com.raywenderlich.android.librarian.ui.bookReviewDetails.animation

sealed class BookReviewDetailsScreenState

object Initial : BookReviewDetailsScreenState()

object Loaded : BookReviewDetailsScreenState()