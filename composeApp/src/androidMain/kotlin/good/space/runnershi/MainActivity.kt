package good.space.runnershi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import good.space.runnershi.service.AndroidServiceController
import good.space.runnershi.shared.di.androidPlatformModule
import good.space.runnershi.shared.di.initKoin
import good.space.runnershi.ui.screen.RunningScreen
import good.space.runnershi.viewmodel.RunningViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initKoin(extraModules = listOf(androidPlatformModule))
        
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. 의존성 주입 (수동)
        val serviceController = AndroidServiceController(this)
        val viewModel = RunningViewModel(serviceController)

        setContent {
            MaterialTheme {
                // 2. 화면 표시
                RunningScreen(viewModel = viewModel)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        // Preview용 더미 ViewModel (실제로는 사용하지 않음)
    }
}