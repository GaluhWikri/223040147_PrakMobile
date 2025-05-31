package id.ac.unpas.mynote.repositories

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import id.ac.unpas.mynote.networks.LoginApi
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: LoginApi
) {
    suspend fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        api.login(email, password)
            .suspendOnSuccess {
                if (data.token != null) {
                    onSuccess(data.token!!)
                } else {
                    onError("Token is null")
                }
            }
            .suspendOnError {
                onError(message())
            }
            .suspendOnException {
                onError(message())
            }
    }
}
