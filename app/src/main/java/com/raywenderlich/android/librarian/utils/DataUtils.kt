package com.raywenderlich.android.librarian.utils

import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

val EMPTY_BOOK = Book("", "", "", "")
val EMPTY_GENRE = Genre("", "")
val EMPTY_BOOK_AND_GENRE = BookAndGenre(EMPTY_BOOK, EMPTY_GENRE)