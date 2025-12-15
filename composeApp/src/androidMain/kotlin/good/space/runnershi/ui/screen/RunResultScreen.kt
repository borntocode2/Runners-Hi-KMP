package good.space.runnershi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import good.space.runnershi.model.domain.RunResult
import good.space.runnershi.util.TimeFormatter

@Composable
fun RunResultScreen(
    result: RunResult,
    onClose: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    // 화면 진입 시 전체 경로가 보이도록 줌 아웃 (LatLngBounds)
    LaunchedEffect(Unit) {
        if (result.pathSegments.flatten().isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()
            result.pathSegments.flatten().forEach { 
                boundsBuilder.include(LatLng(it.latitude, it.longitude)) 
            }
            try {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100) // 100px padding
                )
            } catch (e: Exception) {
                // 경로가 너무 작거나 없을 때 예외 처리
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // [상단] 지도 스냅샷 (조작 불가)
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    myLocationButtonEnabled = false
                )
            ) {
                result.pathSegments.forEach { segment ->
                    if (segment.isNotEmpty()) {
                        Polyline(
                            points = segment.map { LatLng(it.latitude, it.longitude) },
                            color = Color(0xFF6200EE),
                            width = 12f
                        )
                    }
                }
            }
        }

        // [하단] 요약 정보
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Great Run! 🎉", 
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ResultItem("Distance", String.format("%.2f km", result.totalDistanceMeters / 1000))
                ResultItem("Time", TimeFormatter.formatSecondsToTime(result.durationSeconds))
                ResultItem("Pace", result.avgPace)
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("SAVE & CLOSE")
            }
        }
    }
}

@Composable
fun ResultItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label, 
            color = Color.Gray, 
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            value, 
            fontWeight = FontWeight.Bold, 
            style = MaterialTheme.typography.titleLarge
        )
    }
}

