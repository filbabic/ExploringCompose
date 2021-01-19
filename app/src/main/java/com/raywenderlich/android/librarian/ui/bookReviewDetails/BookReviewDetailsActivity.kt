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

package com.raywenderlich.android.librarian.ui.bookReviewDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.*
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.AddReadingEntryDialog
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.ReadingEntries
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_REVIEW
import com.raywenderlich.android.librarian.utils.EMPTY_GENRE
import com.raywenderlich.android.librarian.utils.formatDateToText
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.coil.CoilImage

@AndroidEntryPoint
class BookReviewDetailsActivity : AppCompatActivity() {

  private val bookReviewDetailsViewModel by viewModels<BookReviewDetailsViewModel>()

  companion object {
    private const val KEY_BOOK_REVIEW = "book_review"

    fun getIntent(context: Context, review: BookReview): Intent {
      val intent = Intent(context, BookReviewDetailsActivity::class.java)

      intent.putExtra(KEY_BOOK_REVIEW, review)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val data = intent?.getParcelableExtra<BookReview>(KEY_BOOK_REVIEW)

    if (data == null) {
      finish()
      return
    }

    bookReviewDetailsViewModel.setReview(data)
    setContent {
      LibrarianTheme {
        BookReviewDetailsContent()
      }
    }
  }

  @Composable
  fun BookReviewDetailsContent() {
    val state = transition(
      definition = transitionDefinition,
      toState = true,
      initState = bookReviewDetailsViewModel.isFirstLoadFinishedState.value
        ?: false
    )

    Scaffold(topBar = { BookReviewDetailsTopBar() },
      floatingActionButton = { AddReadingEntry(state) }) {
      BookReviewDetailsInformation(state)
    }
  }

  @Composable
  fun BookReviewDetailsTopBar() {
    val reviewState by bookReviewDetailsViewModel.bookReviewDetailsState.observeAsState()
    val bookName = reviewState?.book?.name
      ?: stringResource(id = R.string.book_review_details_title)

    TopBar(title = bookName, onBackPressed = { onBackPressed() })
  }

  @Composable
  fun AddReadingEntry(state: TransitionState) {
    FloatingActionButton(
      modifier = Modifier.size(state[floatingButtonSize]),
      onClick = { bookReviewDetailsViewModel.onAddEntryTapped() }) {
      Icon(imageVector = Icons.Default.Add)
    }
  }

  @Composable
  fun BookReviewDetailsInformation(state: TransitionState) {
    val bookReview by bookReviewDetailsViewModel.bookReviewDetailsState.observeAsState(
      EMPTY_BOOK_REVIEW
    )

    val genre by bookReviewDetailsViewModel
      .genreState
      .observeAsState(EMPTY_GENRE)

    val deleteEntryState by bookReviewDetailsViewModel.deleteEntryState.observeAsState()
    val isShowingAddEntry by bookReviewDetailsViewModel
      .isShowingAddEntryState.observeAsState(false)

    val entryToDelete = deleteEntryState

    Box(
      modifier = with(BoxScope) {
        Modifier.fillMaxSize()
          .align(Alignment.Center)
      }
    ) {
      ReadingEntries(
        onItemLongClick = { bookReviewDetailsViewModel.onItemLongTapped(it) },
        content = {
          Column(modifier = with(ColumnScope) {
            Modifier.fillMaxSize()
              .align(Alignment.CenterHorizontally)
              .padding(bottom = 16.dp)
          }, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(state[imageMarginTop]))

            Card(
              modifier = Modifier
                .size(width = 200.dp, height = 300.dp),
              shape = RoundedCornerShape(16.dp),
              elevation = 16.dp
            ) {
              CoilImage(
                data = bookReview.review.imageUrl,
                contentScale = ContentScale.FillWidth
              )
            }

            Spacer(modifier = Modifier.height(state[titleMarginTop]))

            Text(
              text = bookReview.book.name,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colors.onPrimary,
              modifier = Modifier.alpha(state[contentAlpha])
            )

            Spacer(modifier = Modifier.height(state[contentMarginTop]))

            Text(
              text = genre.name,
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary,
              modifier = Modifier.alpha(state[contentAlpha])
            )

            Spacer(modifier = Modifier.height(state[contentMarginTop]))

            RatingBar(
              range = 1..5,
              isSelectable = false,
              isLargeRating = false,
              currentRating = bookReview.review.rating
            )

            Spacer(modifier = Modifier.height(state[contentMarginTop]))

            Text(
              text = stringResource(
                id = R.string.last_updated_date,
                formatDateToText(bookReview.review.lastUpdatedDate)
              ),
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary,
              modifier = Modifier.alpha(state[contentAlpha])
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
              modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(
                  brush = SolidColor(value = Color.LightGray),
                  shape = RectangleShape
                )
            )

            Text(
              modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
                .alpha(state[contentAlpha]),
              text = bookReview.review.notes,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic,
              color = MaterialTheme.colors.onPrimary
            )

            Spacer(
              modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(
                  brush = SolidColor(value = Color.LightGray),
                  shape = RectangleShape
                )
            )
          }
        },
        readingEntries = bookReview.review.entries,
      )

      if (isShowingAddEntry) {
        AddReadingEntryDialog(
          onDismiss = { bookReviewDetailsViewModel.onDialogDismiss() },
          onReadingEntryFinished = {
            bookReviewDetailsViewModel.addNewEntry(it)
            bookReviewDetailsViewModel.onDialogDismiss()
          }
        )
      }

      if (entryToDelete != null) {
        DeleteDialog(
          item = entryToDelete,
          message = stringResource(id = R.string.delete_entry_message),
          onDeleteItem = {
            bookReviewDetailsViewModel.removeReadingEntry(it)
            bookReviewDetailsViewModel.onDialogDismiss()
          },
          onDismiss = { bookReviewDetailsViewModel.onDialogDismiss() }
        )
      }
    }
  }
}