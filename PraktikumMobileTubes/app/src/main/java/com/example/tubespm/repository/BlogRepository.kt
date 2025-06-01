package com.example.tubespm.repository

import android.content.SharedPreferences // <-- Pastikan import ini ada
import android.util.Log
import com.example.tubespm.data.dao.ArticleDao
import com.example.tubespm.data.dao.CommentDao
import com.example.tubespm.data.dao.UserDao
import com.example.tubespm.data.model.Article
import com.example.tubespm.data.model.ArticleRequest
import com.example.tubespm.data.model.ArticleResponse
import com.example.tubespm.data.model.AuthResponse
import com.example.tubespm.data.model.Comment
import com.example.tubespm.data.model.LoginRequest
import com.example.tubespm.data.model.RegisterRequest
import com.example.tubespm.network.ApiService
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepository @Inject constructor(
    private val apiService: ApiService,
    private val articleDao: ArticleDao,
    private val commentDao: CommentDao,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences // <-- Inject SharedPreferences di sini
) {

    private val apiUtcDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val apiSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    private fun parseApiDateFlexible(dateString: String?): Date {
        if (dateString.isNullOrBlank()) {
            Log.w("BlogRepository", "Date string is null or blank, returning current date.")
            return Date()
        }
        return try {
            apiUtcDateFormat.parse(dateString) ?: Date()
        } catch (e: Exception) {
            try {
                apiSimpleDateFormat.parse(dateString) ?: Date()
            } catch (e2: Exception) {
                Log.e("BlogRepository", "Could not parse date: '$dateString'. Returning current date.", e2)
                Date()
            }
        }
    }

    private fun mapResponseToArticleEntity(response: ArticleResponse): Article {
        return Article(
            id = response.id.toString(),
            title = response.title,
            content = response.content,
            imageUrl = response.imageUrl,
            createdAt = parseApiDateFlexible(response.createdAtApi ?: response.date),
            updatedAt = parseApiDateFlexible(response.updatedAtApi ?: response.createdAtApi ?: response.date)
        )
    }

    fun getAllArticles(): Flow<List<Article>> = articleDao.getAllArticles()

    suspend fun getArticleById(id: String): Article? {
        var article = articleDao.getArticleById(id)
        if (article != null) return article
        try {
            val response = apiService.getArticle(id)
            if (response.isSuccessful && response.body() != null) {
                val articleEntity = mapResponseToArticleEntity(response.body()!!)
                articleDao.insertArticle(articleEntity)
                return articleEntity
            } else {
                Log.e("BlogRepository", "getArticleById - API Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "getArticleById - Exception", e)
        }
        return null
    }

    suspend fun syncArticles() {
        try {
            Log.d("BlogRepository", "Attempting to sync articles...")
            val response = apiService.getArticles()
            if (response.isSuccessful) {
                response.body()?.let { articleResponses ->
                    if (articleResponses.isEmpty()) {
                        Log.d("BlogRepository", "No articles received from API to sync.")
                    } else {
                        val articlesToInsert = articleResponses.map { mapResponseToArticleEntity(it) }
                        articlesToInsert.forEach { articleDao.insertArticle(it) }
                        Log.d("BlogRepository", "Articles synced successfully: ${articlesToInsert.size} articles.")
                    }
                } ?: Log.d("BlogRepository", "SyncArticles: Response body is null.")
            } else {
                Log.e("BlogRepository", "SyncArticles API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Error during article synchronization", e)
        }
    }

    suspend fun createArticle(request: ArticleRequest): Result<Article> {
        return try {
            val response = apiService.createArticle(request)
            if (response.isSuccessful && response.body() != null) {
                val articleResponse = response.body()!!
                val articleEntity = mapResponseToArticleEntity(articleResponse)
                articleDao.insertArticle(articleEntity)
                Result.success(articleEntity)
            } else {
                val errorMsg = "Failed to create article on server: ${response.code()} ${response.message()} - ${response.errorBody()?.string()}"
                Log.e("BlogRepository", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception creating article", e)
            Result.failure(e)
        }
    }

    suspend fun updateArticle(articleServerId: String, request: ArticleRequest): Result<Article> {
        return try {
            val response = apiService.updateArticle(articleServerId, request)
            if (response.isSuccessful && response.body() != null) {
                val articleResponse = response.body()!!
                val articleEntity = mapResponseToArticleEntity(articleResponse)
                articleDao.updateArticle(articleEntity)
                Result.success(articleEntity)
            } else {
                val errorMsg = "Failed to update article: ${response.code()} ${response.message()} - ${response.errorBody()?.string()}"
                Log.e("BlogRepository", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception updating article", e)
            Result.failure(e)
        }
    }

    suspend fun deleteArticle(article: Article): Result<Unit> {
        return try {
            val response = apiService.deleteArticle(article.id)
            if (response.isSuccessful) {
                articleDao.deleteArticle(article)
                Result.success(Unit)
            } else {
                Log.e("BlogRepository", "Failed to delete article on server: ${response.code()} ${response.message()}")
                Result.failure(Exception("Failed to delete article on server"))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception deleting article", e)
            Result.failure(e)
        }
    }

    fun getCommentsByArticleId(articleId: String): Flow<List<Comment>> =
        commentDao.getCommentsByArticleId(articleId)

    suspend fun createComment(comment: Comment): Result<Comment> {
        return try {
            val response = apiService.createComment(comment.articleId, comment)
            if (response.isSuccessful && response.body() != null) {
                commentDao.insertComment(response.body()!!)
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create comment: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(comment: Comment): Result<Unit> {
        return try {
            commentDao.deleteComment(comment)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(loginRequest: LoginRequest): Result<AuthResponse> {
        // ViewModel akan menangani penyimpanan token dari hasil fungsi ini.
        // Repository hanya meneruskan hasil dari API.
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown login error"
                Log.e("BlogRepository", "Login failed: ${response.code()} - $errorBody")
                Result.failure(Exception("Login failed: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception during login", e)
            Result.failure(e)
        }
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Result<AuthResponse> {
        // ViewModel akan menangani penyimpanan token dari hasil fungsi ini.
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown registration error"
                Log.e("BlogRepository", "Registration failed: ${response.code()} - $errorBody")
                Result.failure(Exception("Registration failed: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception during registration", e)
            Result.failure(e)
        }
    }

    suspend fun logoutUser(): Result<Unit> {
        return try {
            // PENTING: OkHttpClient Anda (via AuthInterceptor di NetworkModule)
            // harus menambahkan Authorization Bearer token ke request ini.
            val response = apiService.logout()
            if (response.isSuccessful) {
                // Hapus token dan data sesi pengguna yang tersimpan secara lokal
                sharedPreferences.edit().apply {
                    remove("auth_token")
                    // Anda mungkin juga ingin menghapus ID pengguna atau data pengguna lain yang disimpan
                    // remove("user_id")
                }.apply() // Pastikan .apply() dipanggil setelah konfigurasi edit selesai
                Log.d("BlogRepository", "Logout API call successful. Local token and session cleared.")
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown logout error"
                Log.e("BlogRepository", "Logout API call failed: ${response.code()} - $errorBody")
                Result.failure(Exception("Logout API call failed: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("BlogRepository", "Exception during logout: ${e.message}", e)
            Result.failure(Exception("Exception during logout: ${e.message}", e))
        }
    }
}