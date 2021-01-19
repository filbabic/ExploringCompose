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

package com.raywenderlich.android.librarian.repository

import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LibrarianRepositoryImpl @Inject constructor(
  private val bookDao: BookDao,
  private val reviewDao: ReviewDao,
  private val readingListDao: ReadingListDao,
  private val genreDao: GenreDao
) : LibrarianRepository {

  override fun clearData() {
    bookDao.clearData()
    reviewDao.clearData()
    readingListDao.clearData()
    genreDao.clearData()
  }

  override fun insertDummyData(
    books: List<Book>,
    reviews: List<Review>,
    readingLists: List<ReadingList>,
    genres: List<Genre>
  ) {
    bookDao.insertData(books)
    reviewDao.insertData(reviews)
    readingListDao.insertData(readingLists)
    genreDao.insertData(genres)
  }

  override suspend fun removeBook(book: Book) = bookDao.removeBook(book)

  override fun getBooksFlow(): Flow<List<BookAndGenre>> = bookDao.getBooksFlow()

  private suspend fun getBookById(bookId: String): BookAndGenre = bookDao.getBookById(bookId)

  override fun getReviewsFlow(): Flow<List<BookReview>> = reviewDao.getReviewsFlow()

  override suspend fun removeReview(review: Review) = reviewDao.removeReview(review)

  override suspend fun getReviewById(reviewId: String): BookReview =
    reviewDao.getReviewsById(reviewId)

  override suspend fun updateReview(review: Review) = reviewDao.updateReview(review)

  override suspend fun getGenreById(genreId: String): Genre = genreDao.getGenreById(genreId)

  override fun getReadingListsFlow(): Flow<List<ReadingListsWithBooks>> =
    readingListDao.getReadingListsFlow()
      .map { items ->
        items.map { readingList ->
          ReadingListsWithBooks(readingList.id, readingList.name,
            readingList.bookIds.map { bookId ->
              getBookById(bookId)
            })
        }
      }

  override suspend fun removeReadingList(readingList: ReadingList) =
    readingListDao.removeReadingList(readingList)

  override suspend fun addReadingList(readingList: ReadingList) =
    readingListDao.addReadingList(readingList)
}