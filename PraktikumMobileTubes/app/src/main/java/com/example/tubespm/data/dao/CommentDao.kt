package com.example.tubespm.data.dao

// CommentDao.kt
import androidx.room.*
import com.example.tubespm.data.model.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE articleId = :articleId ORDER BY createdAt DESC")
    fun getCommentsByArticleId(articleId: String): Flow<List<Comment>>

    @Insert
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)
}