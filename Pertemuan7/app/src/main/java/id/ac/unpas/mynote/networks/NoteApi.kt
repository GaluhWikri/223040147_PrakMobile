package id.ac.unpas.mynote.networks

import com.skydoves.sandwich.ApiResponse
import id.ac.unpas.mynote.models.Note
import retrofit2.http.*

interface NoteApi {

    @GET("api/v2/notes")
    suspend fun findAll(
        @Header("Authorization") token: String
    ): ApiResponse<NoteListResponse>

    @GET("api/v2/notes/{id}")
    suspend fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ApiResponse<NoteSingleResponse>

    @POST("api/v2/notes")
    @Headers("Content-Type: application/json")
    suspend fun insert(
        @Header("Authorization") token: String,
        @Body data: Note
    ): ApiResponse<NoteSingleResponse>

    @PUT("api/v2/notes/{id}")
    @Headers("Content-Type: application/json")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: Note
    ): ApiResponse<NoteSingleResponse>

    @DELETE("api/v2/notes/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ApiResponse<DeleteResponse>
}
