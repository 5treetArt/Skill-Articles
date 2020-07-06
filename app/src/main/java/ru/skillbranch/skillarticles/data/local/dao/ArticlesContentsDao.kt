package ru.skillbranch.skillarticles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface ArticlesContentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: ArticleContent): Long
}