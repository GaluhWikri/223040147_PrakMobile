package id.ac.unpas.mynote.networks

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String?
)
