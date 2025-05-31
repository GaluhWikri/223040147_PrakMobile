package id.ac.unpas.mynote.networks

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi {
    @POST("api/v2/login")
    @FormUrlEncoded
    suspend fun login(@Field("email") email: String, @Field("password") password: String): ApiResponse<LoginResponse>
}
