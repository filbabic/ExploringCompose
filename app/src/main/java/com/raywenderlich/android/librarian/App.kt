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

package com.raywenderlich.android.librarian

import android.app.Application
import com.google.gson.Gson
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

  @Inject
  lateinit var repository: LibrarianRepository

  companion object {
    private lateinit var instance: App

    val gson by lazy { Gson() }
  }

  override fun onCreate() {
    super.onCreate()
    instance = this

    GlobalScope.launch {
      repository.clearData()

      val genres = listOf(
        Genre(name = "Action"),
        Genre(name = "Adventure"),
        Genre(name = "Classic"),
        Genre(name = "Mystery"),
        Genre(name = "Fantasy"),
        Genre(name = "Sci-Fi"),
        Genre(name = "History"),
        Genre(name = "Horror"),
        Genre(name = "Romance"),
        Genre(name = "Short Story"),
        Genre(name = "Biography"),
        Genre(name = "Poetry"),
        Genre(name = "Self-Help"),
        Genre(name = "Young fiction")
      )

      val books = listOf(
        Book(
          name = "At The Mountains Of Madness",
          description = "Fear of the unknown... And there is a lot of unknown in the icy depths of the Arctic. Some things should be left sleeping.",
          genreId = genres.first { it.name == "Horror" }.id
        ),
        Book(
          name = "Dream Quest Of Unknown Kadath",
          description = "Randolph Carter takes us on a dream journey we never could've imagined.",
          genreId = genres.first { it.name == "Horror" }.id
        ),
        Book(
          name = "The Last Wish",
          description = "Geralt of Rivia is a legendary figure. A skilled monster slayer, empowered by powerful and secret mutations from his very childhood. Join him while we tackles foes large and small, quick and deadly.",
          genreId = genres.first { it.name == "Fantasy" }.id
        )
      )

      val readingLists = listOf(
        ReadingList(
          name = "Eldritch Horror",
          bookIds = listOf(
            books.first { it.name == "At The Mountains Of Madness" }.id,
            books.first { it.name == "Dream Quest Of Unknown Kadath" }.id
          )
        ),
        ReadingList(name = "Romantic Evening", bookIds = emptyList()),
        ReadingList(
          name = "The Sword Of Destiny",
          bookIds = listOf(books.first { it.name == "The Last Wish" }.id)
        )
      )

      val reviews = listOf(
        Review(
          bookId = books.first { it.name == "At The Mountains Of Madness" }.id,
          rating = 4,
          imageUrl = "https://m.media-amazon.com/images/I/61LFSHVx8gL.jpg",
          notes = "A really interesting book that picks your brain about what kinds of hidden and hideous things could exist in this huge and unexplored world."
        ),
        Review(
          bookId = books.first { it.name == "Dream Quest Of Unknown Kadath" }.id,
          rating = 5,
          imageUrl = "https://d3525k1ryd2155.cloudfront.net/h/474/226/988226474.0.x.1.jpg",
          notes = "Another beautiful yet horrifying story by Lovecraft, which takes you on multiple journeys"
        ),
        Review(
          bookId = books.first { it.name == "The Last Wish" }.id,
          rating = 5,
          imageUrl = "https://www.hachettebookgroup.com/wp-content/uploads/2019/09/9780316497541-1.jpg?fit=424%2C675",
          notes = "The Witcher universe is simply stunning and exhilarating. The spectrum of vicious and deadly monsters towards simple people with emotions, feelings and their own everyday struggles makes the fantasy part of the book fly much lower and makes it easier to recognize our own traits and flaws in such characters."
        )
      )

      repository.insertDummyData(
        genres = genres,
        books = books,
        readingLists = readingLists,
        reviews = reviews
      )
    }
  }
}