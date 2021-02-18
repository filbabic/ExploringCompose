package com.raywenderlich.android.librarian.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
  repository: LibrarianRepository
) : ViewModel() {

  val booksState = repository.getBooksFlow().asLiveData(viewModelScope.coroutineContext)

  val selectedBooks = MutableLiveData(emptyList<String>())

  fun onBookLongTapped(bookAndGenre: BookAndGenre) {
    val selectedBooks = selectedBooks.value ?: emptyList()

    if (bookAndGenre.book.id in selectedBooks) {
      this.selectedBooks.value = selectedBooks - bookAndGenre.book.id
    } else {
      this.selectedBooks.value = selectedBooks + bookAndGenre.book.id
    }
  }
}