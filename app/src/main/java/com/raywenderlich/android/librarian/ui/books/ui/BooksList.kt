package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BooksList(
  books: List<BookAndGenre> = emptyList(),
  selectedBooks: List<String>,
  onLongItemTap: (BookAndGenre) -> Unit = {}
) {

  LazyColumn(
    modifier = Modifier.padding(top = 16.dp)
  ) {
    items(items = books) { bookAndGenre ->
      BookListItem(bookAndGenre, bookAndGenre.book.id in selectedBooks, onLongItemTap)
      Spacer(modifier = Modifier.size(2.dp))
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookListItem(
  bookAndGenre: BookAndGenre,
  isSelected: Boolean,
  onLongItemTap: (BookAndGenre) -> Unit
) {
  val cornerRadius by animateDpAsState(targetValue = if (isSelected) 0.dp else 16.dp)
  val borderColor by
  animateColorAsState(targetValue = if (isSelected) Color.Red else MaterialTheme.colors.primary)
  val textColor by
  animateColorAsState(targetValue = if (isSelected) Color.Red else MaterialTheme.colors.primary)

  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
      .combinedClickable(onClick = {},
        onLongClick = { onLongItemTap(bookAndGenre) }),
    elevation = 8.dp,
    border = BorderStroke(1.dp, borderColor),
    shape = RoundedCornerShape(cornerRadius)
  ) {

    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(
          text = bookAndGenre.book.name,
          modifier = Modifier.padding(top = 16.dp),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = textColor
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = bookAndGenre.genre.name,
          fontStyle = FontStyle.Italic,
          fontSize = 16.sp,
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = bookAndGenre.book.description,
          fontStyle = FontStyle.Italic,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.fillMaxHeight().padding(end = 16.dp),
          fontSize = 12.sp,
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}