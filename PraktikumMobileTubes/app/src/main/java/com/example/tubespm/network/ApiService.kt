package com.example.tubespm.network

import com.example.tubespm.data.model.ArticleRequest
import com.example.tubespm.data.model.ArticleResponse
import com.example.tubespm.data.model.AuthResponse
import com.example.tubespm.data.model.Comment
import com.example.tubespm.data.model.LoginRequest
import com.example.tubespm.data.model.RegisterRequest // <-- Tambahkan import ini
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("articles")
    suspend fun getArticles(): Response<List<ArticleResponse>>

    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: String): Response<ArticleResponse>

    @POST("articles")
    suspend fun createArticle(@Body articleRequest: ArticleRequest): Response<ArticleResponse>

    @PUT("articles/{id}")
    suspend fun updateArticle(@Path("id") id: String, @Body articleRequest: ArticleRequest): Response<ArticleResponse>

    @DELETE("articles/{id}")
    suspend fun deleteArticle(@Path("id") id: String): Response<Unit>

    @GET("articles/{id}/comments")
    suspend fun getComments(@Path("id") articleId: String): Response<List<Comment>>

    @POST("articles/{id}/comments")
    suspend fun createComment(@Path("id") articleId: String, @Body comment: Comment): Response<Comment>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("register") // <-- Endpoint baru untuk register
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("logout")
    suspend fun logout(): Response<Unit>
}