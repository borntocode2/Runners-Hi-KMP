package good.space.runnershi.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import good.space.runnershi.auth.TokenStorage
import good.space.runnershi.model.dto.auth.LoginRequest
import good.space.runnershi.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoading: Boolean = false
)

sealed interface LoginSideEffect {
    data object NavigateToHome : LoginSideEffect
    data object NavigateToSignUp : LoginSideEffect
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<LoginSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null)
        }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (currentState.email.isBlank()) {
            _uiState.update {
                it.copy(emailError = "이메일을 입력해주세요.")
            }
            return
        }
        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(passwordError = "비밀번호를 입력해주세요.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    emailError = null,
                    passwordError = null,
                    loginError = null
                )
            }

            val loginResult = authRepository.login(
                LoginRequest(
                    email = currentState.email,
                    password = currentState.password
                )
            )

            loginResult.onSuccess { response ->
                tokenStorage.saveTokens(response.accessToken, response.refreshToken)
                _uiState.update { it.copy(isLoading = false) }
                _sideEffect.send(LoginSideEffect.NavigateToHome)
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = "이메일과 비밀번호를 확인해주세요."
                    )
                }
            }
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _sideEffect.send(LoginSideEffect.NavigateToSignUp)
        }
    }
}
