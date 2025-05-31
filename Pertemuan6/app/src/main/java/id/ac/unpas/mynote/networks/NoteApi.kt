package id.ac.unpas.mynote.networks

import com.skydoves.sandwich.ApiResponse
import id.ac.unpas.mynote.models.Note
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {
    @GET("api/notes")
    suspend fun all(): ApiResponse<NoteListResponse>

    @GET("api/notes/{id}")
    suspend fun findById(@Path("id") id: String): ApiResponse<NoteSingleResponse>

    @POST("api/notes")
    @Headers("Content-Type: application/json")
    suspend fun insert(@Body data: Note): ApiResponse<NoteSingleResponse>

    @PUT("api/notes/{id}")
    @Headers("Content-Type: application/json")
    suspend fun update(@Path("id") id: String, @Body data: Note): ApiResponse<NoteSingleResponse>

    @DELETE("api/notes/{id}")
    suspend fun delete(@Path("id") id: String): ApiResponse<DeleteResponse>
}
