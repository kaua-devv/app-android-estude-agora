package com.app.estudeagora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.estudeagora.model.enums.DifficultLearnEnum
import com.app.estudeagora.ui.theme.DifficultyEasy
import com.app.estudeagora.ui.theme.DifficultyHard
import com.app.estudeagora.ui.theme.DifficultyNormal

@Composable
fun DifficultyBadge(
    difficult: DifficultLearnEnum,
    modifier: Modifier = Modifier
) {
    val (label, color) = when (difficult) {
        DifficultLearnEnum.EASY -> "Fácil" to DifficultyEasy
        DifficultLearnEnum.MEDIUM -> "Médio" to DifficultyNormal
        DifficultLearnEnum.HARD -> "Difícil" to DifficultyHard
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.16f))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
