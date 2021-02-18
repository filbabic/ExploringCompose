package com.raywenderlich.android.librarian.ui.bookReviewDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.BookReviewDetailsScreenState
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.Initial
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.Loaded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookReviewDetailsViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _bookReviewDetailsState = MutableLiveData<BookReview>()
  val bookReviewDetailsState: LiveData<BookReview> = _bookReviewDetailsState

  private val _genreState = MutableLiveData<Genre>()
  val genreState: LiveData<Genre> = _genreState

  private val _deleteEntryState = MutableLiveData<ReadingEntry?>()
  val deleteEntryState: LiveData<ReadingEntry?> = _deleteEntryState

  private val _isShowingAddEntryState = MutableLiveData<Boolean>()
  val isShowingAddEntryState: LiveData<Boolean> = _isShowingAddEntryState

  private val _screenAnimationState = MutableLiveData<BookReviewDetailsScreenState>()
  val screenAnimationState: LiveData<BookReviewDetailsScreenState> = _screenAnimationState

  fun setReview(bookReview: BookReview) {
    _bookReviewDetailsState.value = bookReview

    viewModelScope.launch {
      _genreState.value = repository.getGenreById(bookReview.book.genreId)
    }
  }

  fun addNewEntry(entry: String) {
    val data = _bookReviewDetailsState.value?.review ?: return
    val updatedReview = data.copy(
      entries = data.entries + ReadingEntry(comment = entry),
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
    onDialogDismiss()
  }

  fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = _bookReviewDetailsState.value?.review ?: return

    val updatedReview = data.copy(
      entries = data.entries - readingEntry,
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
    onDialogDismiss()
  }

  fun onDialogDismiss() {
    _deleteEntryState.value = null
    _isShowingAddEntryState.value = false
  }

  fun onAddEntryTapped() {
    _isShowingAddEntryState.value = true
  }

  fun onItemLongTapped(readingEntry: ReadingEntry) {
    _deleteEntryState.value = readingEntry
  }

  private fun updateReview(updatedReview: Review) {
    viewModelScope.launch {
      repository.updateReview(updatedReview)
      setReview(repository.getReviewById(updatedReview.id))
    }
  }

  fun onScreenLoaded() {
    _screenAnimationState.value = Loaded
  }

  fun animateReverse() {
    _screenAnimationState.value = Initial
  }
}