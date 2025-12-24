package good.space.runnershi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import good.space.runnershi.ui.theme.RunnersHiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ButtonStyle {
    FILLED, OUTLINED
}

@Composable
fun RunnersHiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.FILLED,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor(style),
            contentColor = contentColor(style),
            disabledContainerColor = RunnersHiTheme.custom.inputDisable,
            disabledContentColor = RunnersHiTheme.custom.inputDisableBorder
        ),
        border = border(style),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = null
    ) {
        Text(
            text = text,
            style = RunnersHiTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        )
    }
}

@Composable
@ReadOnlyComposable
private fun containerColor(style: ButtonStyle): Color {
    return when(style) {
        ButtonStyle.FILLED -> RunnersHiTheme.colorScheme.primary
        ButtonStyle.OUTLINED -> RunnersHiTheme.colorScheme.onPrimary
    }
}

@Composable
@ReadOnlyComposable
private fun contentColor(style: ButtonStyle): Color {
    return when(style) {
        ButtonStyle.FILLED -> RunnersHiTheme.colorScheme.onPrimary
        ButtonStyle.OUTLINED -> RunnersHiTheme.colorScheme.primary
    }
}

@Composable
@ReadOnlyComposable
private fun border(style: ButtonStyle): BorderStroke? {
    return when(style) {
        ButtonStyle.FILLED -> null
        ButtonStyle.OUTLINED -> BorderStroke(
            width = 1.dp,
            color = RunnersHiTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun RunnersHiButtonPreview() {
    RunnersHiTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            RunnersHiButton(
                text = "Log In",
                onClick = {},
                style = ButtonStyle.FILLED
            )

            Spacer(modifier = Modifier.height(16.dp))

            RunnersHiButton(
                text = "Sign Up",
                onClick = {},
                style = ButtonStyle.OUTLINED
            )
        }
    }
}
