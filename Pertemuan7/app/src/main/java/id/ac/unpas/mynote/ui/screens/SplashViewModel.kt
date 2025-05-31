package id.ac.unpas.mynote.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.mynote.repositories.SessionRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    fun checkSession(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            sessionRepository.token
                .catch { onError() }
                .collect { token ->
                    if (token.isNotEmpty()) {
                        onSuccess()
                    } else {
                        onError()
                    }
                }
        }
    }
}
