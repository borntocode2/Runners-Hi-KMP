package good.space.runnershi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import good.space.runnershi.ui.theme.RunnersHiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsPopup(
    isAutoPauseEnabled: Boolean,
    offset: IntOffset = IntOffset(x = 120, y = -120),
    onDismissRequest: () -> Unit,
    onToggleAutoPause: () -> Unit,
    onLogout: () -> Unit
) {
    Popup(
        alignment = Alignment.BottomStart,
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = RunnersHiTheme.colorScheme.background
            ),
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(vertical = 8.dp)
            ) {
                SettingsMenuItem(
                    icon = if (isAutoPauseEnabled) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                    text = if (isAutoPauseEnabled) "자동 일시정지 끄기" else "자동 일시정지 켜기",
                    onClick = onToggleAutoPause
                )

                SettingsMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    text = "로그아웃",
                    onClick = {
                        onLogout()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF424242),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242)
        )
    }
}

@Preview
@Composable
fun PreviewSettingsPopupWithAnchor() {
    RunnersHiTheme {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(Color(0xFFF0F0F0))
                .padding(20.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Box {
                IconButton(
                    onClick = { },
                    modifier = Modifier.background(Color.White, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "설정",
                        tint = Color.Gray
                    )
                }

                SettingsPopup(
                    isAutoPauseEnabled = false,
                    onDismissRequest = {},
                    onToggleAutoPause = {},
                    onLogout = {}
                )
            }
        }
    }
}
