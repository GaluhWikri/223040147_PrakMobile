package id.ac.unpas.mynote.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.mynote.repositories.LoginRepository
import id.ac.unpas.mynote.repositories.SessionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _message: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> get() = _message

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isEmpty()) {
            _message.postValue("Email harus diisi")
            return
        }

        if (password.isEmpty()) {
            _message.postValue("Password harus diisi")
            return
        }

        viewModelScope.launch {
            loginRepository.login(email, password,
                onSuccess = {
                    viewModelScope.launch {
                        sessionRepository.setToken(it)
                    }
                    onSuccess()
                },
                onError = {
                    _message.postValue(it)
                }
            )
        }
    }
}
