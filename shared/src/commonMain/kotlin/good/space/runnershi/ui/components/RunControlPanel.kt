package good.space.runnershi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import good.space.runnershi.ui.theme.RunnersHiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RunControlPanel(
    distance: String,
    pace: String,
    time: String,
    isRunning: Boolean,
    onFinishClick: () -> Unit,
    onPauseResumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 정보 표시 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RunStat(label = "거리", value = distance)
                RunStat(label = "페이스", value = pace)
                RunStat(label = "시간", value = time)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 버튼 영역
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 종료 버튼
                GradientCircleButton(
                    buttonColor = GradientCircleButtonColor.BLACK,
                    buttonIcon = GradientCircleButtonIcon.STOP,
                    onClick = onFinishClick,
                    size = 80.dp
                )

                Spacer(modifier = Modifier.width(48.dp))

                // 일시정지 or 재개 버튼
                if (isRunning) {
                    GradientCircleButton(
                        buttonColor = GradientCircleButtonColor.YELLOW,
                        buttonIcon = GradientCircleButtonIcon.PAUSE,
                        onClick = onPauseResumeClick,
                        size = 80.dp
                    )
                } else {
                    GradientCircleButton(
                        buttonColor = GradientCircleButtonColor.GREEN,
                        buttonIcon = GradientCircleButtonIcon.START,
                        onClick = onPauseResumeClick,
                        size = 80.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun RunStat(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color.Gray
        )
    }
}

@Preview
@Composable
private fun RunControlPanelPreview_Running() {
    RunnersHiTheme {
        RunControlPanel(
            distance = "4.02 km",
            pace = "5'23''",
            time = "13:10",
            isRunning = true,
            onFinishClick = {},
            onPauseResumeClick = {}
        )
    }
}

@Preview
@Composable
private fun RunControlPanelPreview_Paused() {
    RunnersHiTheme {
        RunControlPanel(
            distance = "4.02 km",
            pace = "5'23''",
            time = "13:10",
            isRunning = false,
            onFinishClick = {},
            onPauseResumeClick = {}
        )
    }
}
