package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R

@Composable
fun <T> DeleteDialog(
  item: T,
  message: String,
  onDeleteItem: (T) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = stringResource(id = R.string.delete_title)) },
    text = { Text(text = message) },
    buttons = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        DialogButton(
          text = R.string.yes,
          onClickAction = { onDeleteItem(item) })

        DialogButton(
          text = R.string.cancel,
          onClickAction = { onDismiss() })
      }
    },
  )
}