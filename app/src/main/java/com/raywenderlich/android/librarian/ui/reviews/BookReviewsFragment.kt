/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.librarian.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.reviews.ui.BookReviewsList
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fetches and displays notes from the API.
 */

@AndroidEntryPoint
class BookReviewsFragment : Fragment() {

  private val bookReviewsViewModel by viewModels<BookReviewsViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          BookReviewsContent()
        }
      }
    }
  }

  @Composable
  fun BookReviewsContent() {
    Scaffold(
      topBar = { BookReviewsTopBar() },
      floatingActionButton = { AddBookReview() }
    ) {
      BookReviewsContentWrapper()
    }
  }

  @Composable
  fun BookReviewsTopBar() {
    TopBar(title = stringResource(id = R.string.book_reviews_title))
  }

  @Composable
  fun AddBookReview() {
    FloatingActionButton(onClick = { }) {
      Icon(imageVector = Icons.Default.Add)
    }
  }

  @Composable
  fun BookReviewsContentWrapper() {
    val bookReviews by bookReviewsViewModel.bookReviewsState.observeAsState(emptyList())
    val deleteReviewState by bookReviewsViewModel.deleteReviewState.observeAsState()

    val reviewToDelete = deleteReviewState

    Box(
      modifier = with(BoxScope) {
        Modifier
          .align(Alignment.Center)
          .fillMaxSize()
      }
    ) {
      BookReviewsList(bookReviews,
        onItemClick = {},
        onLongItemTap = { bookReview -> bookReviewsViewModel.onItemLongTapped(bookReview) })

      if (reviewToDelete != null) {
        DeleteDialog(
          item = reviewToDelete,
          message = stringResource(
            id =
            R.string.delete_review_message, reviewToDelete.book.name
          ),
          onDeleteItem = { bookReview ->
            bookReviewsViewModel.deleteReview(bookReview)
            bookReviewsViewModel.onDialogDismissed()
          },
          onDismiss = { bookReviewsViewModel.onDialogDismissed() }
        )
      }
    }
  }
}