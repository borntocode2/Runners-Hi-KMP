package good.space.runnershi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import good.space.runnershi.auth.TokenStorage
import good.space.runnershi.model.dto.user.QuestResponse
import good.space.runnershi.repository.AuthRepository
import good.space.runnershi.repository.LocalRunningDataSource
import good.space.runnershi.repository.QuestRepository
import good.space.runnershi.settings.SettingsRepository
import good.space.runnershi.state.RunningStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val quests: List<QuestResponse> = emptyList(),
    val errorMessage: String? = null,
    val isAutoPauseEnabled: Boolean = true
)

class HomeViewModel(
    private val questRepository: QuestRepository,
    private val settingsRepository: SettingsRepository?,
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage,
    private val runningDataSource: LocalRunningDataSource?
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun fetchQuestData() {
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = questRepository.getQuestData()

            result.onSuccess { questList ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        quests = questList
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "퀘스트를 불러오는데 실패했습니다."
                    )
                }
            }
        }
    }

    private fun loadSettings() {
        if (settingsRepository != null) {
            viewModelScope.launch {
                val isEnabled = settingsRepository.isAutoPauseEnabled()
                _uiState.update { it.copy(isAutoPauseEnabled = isEnabled) }
            }
        }
    }

    fun toggleAutoPause() {
        if (settingsRepository != null) {
            viewModelScope.launch {
                val newValue = !_uiState.value.isAutoPauseEnabled
                settingsRepository.setAutoPauseEnabled(newValue)
                _uiState.update { it.copy(isAutoPauseEnabled = newValue) }
            }
        }
    }

    suspend fun logout() {
        // 1. 서버에 로그아웃 요청 (실패해도 로컬 로그아웃은 진행)
        authRepository.logout()
            .onFailure { e ->
                // 서버 로그아웃 실패 시에도 로컬 로그아웃은 진행
                println("⚠️ 서버 로그아웃 실패: ${e.message}")
            }

        // 2. 로컬 DB의 모든 러닝 데이터 삭제 (로그아웃 시 모든 미완료 데이터 제거)
        runningDataSource?.discardAllRuns()

        // 3. 러닝 상태 초기화 (시간, 거리, 경로 등 모든 러닝 정보 리셋)
        RunningStateManager.reset()

        // 4. 로컬 토큰 삭제 (AccessToken과 RefreshToken 제거)
        tokenStorage.clearTokens()
    }
}
