package good.space.runnershi.viewmodel

import good.space.runnershi.auth.TokenStorage
import good.space.runnershi.network.ApiClient
import good.space.runnershi.repository.AuthRepository
import good.space.runnershi.settings.SettingsRepository
import good.space.runnershi.state.RunningStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AppState {
    object Loading : AppState()
    object LoggedIn : AppState()
    object NeedsLogin : AppState()
}

class MainViewModel(
    private val tokenStorage: TokenStorage,
    private val apiClient: ApiClient,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository? = null // [New] 옵셔널로 추가 (iOS 호환성)
) {
    // 로그아웃 시 DB 데이터 삭제를 위한 콜백
    var onLogoutCallback: (suspend () -> Unit)? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    // [New] 오토 퍼즈 설정 상태
    private val _isAutoPauseEnabled = MutableStateFlow(true)
    val isAutoPauseEnabled: StateFlow<Boolean> = _isAutoPauseEnabled.asStateFlow()

    init {
        checkLoginStatus()
        observeAuthErrors()
        // 앱 시작 시 저장된 설정 불러오기
        loadSettings()
    }
    
    // [New] 설정 불러오기
    private fun loadSettings() {
        if (settingsRepository != null) {
            scope.launch {
                _isAutoPauseEnabled.value = settingsRepository.isAutoPauseEnabled()
            }
        }
    }
    
    // [New] 오토 퍼즈 설정 토글
    fun toggleAutoPause() {
        if (settingsRepository != null) {
            scope.launch {
                val newValue = !_isAutoPauseEnabled.value
                settingsRepository.setAutoPauseEnabled(newValue)
                _isAutoPauseEnabled.value = newValue
            }
        }
    }

    // [초기 실행] 토큰 확인
    private fun checkLoginStatus() {
        scope.launch {
            val token = tokenStorage.getAccessToken()
            if (token.isNullOrEmpty()) {
                _appState.value = AppState.NeedsLogin
            } else {
                _appState.value = AppState.LoggedIn
            }
        }
    }

    // [실시간 감지] 리프레시 토큰 만료로 인한 강제 로그아웃 감지
    private fun observeAuthErrors() {
        scope.launch {
            apiClient.authErrorFlow.collect {
                _appState.value = AppState.NeedsLogin
            }
        }
    }
    
    // 로그인 성공 시 호출
    fun onLoginSuccess() {
        _appState.value = AppState.LoggedIn
    }
    
    // 로그아웃 처리
    suspend fun logout() {
        // 1. 서버에 로그아웃 요청 (실패해도 로컬 로그아웃은 진행)
        authRepository.logout()
            .onFailure { e ->
                // 서버 로그아웃 실패 시에도 로컬 로그아웃은 진행
                println("⚠️ 서버 로그아웃 실패: ${e.message}")
            }
        
        // 2. Room DB의 러닝 데이터 삭제 (로그아웃 시 모든 미완료 데이터 제거)
        onLogoutCallback?.invoke()
        
        // 3. 러닝 상태 초기화 (시간, 거리, 경로 등 모든 러닝 정보 리셋)
        RunningStateManager.reset()
        
        // 4. 로컬 토큰 삭제 (AccessToken과 RefreshToken 제거)
        tokenStorage.clearTokens()
        
        // 5. 토큰이 정말 삭제되었는지 확인 후 상태 변경
        val token = tokenStorage.getAccessToken()
        if (token.isNullOrEmpty()) {
            _appState.value = AppState.NeedsLogin
        } else {
            // 토큰이 아직 남아있다면 다시 삭제 시도
            tokenStorage.clearTokens()
            _appState.value = AppState.NeedsLogin
        }
    }
}

