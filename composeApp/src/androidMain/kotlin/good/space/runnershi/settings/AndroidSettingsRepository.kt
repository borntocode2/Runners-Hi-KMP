package good.space.runnershi.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidSettingsRepository(
    context: Context
) : SettingsRepository {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "app_settings",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_AUTO_PAUSE_ENABLED = "auto_pause_enabled"
        private const val DEFAULT_AUTO_PAUSE_ENABLED = true // 기본값: 활성화
    }
    
    override suspend fun isAutoPauseEnabled(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(KEY_AUTO_PAUSE_ENABLED, DEFAULT_AUTO_PAUSE_ENABLED)
    }
    
    override suspend fun setAutoPauseEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit {
            putBoolean(KEY_AUTO_PAUSE_ENABLED, enabled)
        }
    }
    
    /**
     * 동기 버전: SharedPreferences는 메모리 캐시를 사용하므로 매우 빠릅니다.
     * UI 스레드에서 호출해도 안전하며, Race Condition을 방지하기 위해 사용합니다.
     */
    fun isAutoPauseEnabledSync(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_PAUSE_ENABLED, DEFAULT_AUTO_PAUSE_ENABLED)
    }
}

