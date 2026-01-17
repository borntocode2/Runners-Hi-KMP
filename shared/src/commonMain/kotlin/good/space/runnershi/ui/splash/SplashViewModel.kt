package good.space.runnershi.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import good.space.runnershi.auth.TokenStorage
import good.space.runnershi.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val tokenStorage: TokenStorage,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                val refreshToken = tokenStorage.getRefreshToken()
                if (refreshToken == null) {
                    _isLoggedIn.value = false
                    return@launch
                }

                val result = authRepository.refreshAccessToken(refreshToken)
                result.onSuccess {
                    tokenStorage.saveTokens(it.accessToken, it.refreshToken)
                    _isLoggedIn.value = true
                }.onFailure {
                    _isLoggedIn.value = false
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
