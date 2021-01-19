package com.raywenderlich.android.librarian.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raywenderlich.android.librarian.model.Genre

@Dao
interface GenreDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertData(data: List<Genre>)

  @Query("SELECT * FROM genre WHERE id = :genreId")
  suspend fun getGenreById(genreId: String): Genre

  @Query("DELETE FROM genre")
  fun clearData()
}