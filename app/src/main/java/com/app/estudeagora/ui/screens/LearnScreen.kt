package com.app.estudeagora.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.estudeagora.controller.LearnViewModel
import com.app.estudeagora.model.entity.Learn
import com.app.estudeagora.model.enums.DifficultLearnEnum
import com.app.estudeagora.ui.components.DifficultyBadge
import com.app.estudeagora.ui.theme.EstudeAgoraTheme

private val ContentMaxWidth = 600.dp
private val WideMaxWidth = 960.dp
private val WideBreakpoint = 700.dp

// Com estado: pega o ViewModel e observa o StateFlow.
@Composable
fun LearnScreen(
    modifier: Modifier = Modifier,
    viewModel: LearnViewModel = hiltViewModel()
) {
    val learn by viewModel.learn.collectAsStateWithLifecycle()
    // Sobrevive à rotação da tela.
    var revealed by rememberSaveable { mutableStateOf(false) }

    LearnContent(
        learn = learn,
        revealed = revealed,
        onDraw = {
            viewModel.random()
            revealed = true
        },
        modifier = modifier
    )
}

// Sem estado: só recebe dados e callbacks (bom para @Preview).
@Composable
fun LearnContent(
    learn: Learn?,
    revealed: Boolean,
    onDraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val compact = maxWidth < 360.dp

        Crossfade(
            targetState = revealed,
            modifier = Modifier.fillMaxSize(),
            label = "intro-resultado"
        ) { showResult ->
            if (showResult) {
                ResultPage(learn = learn, onDrawAgain = onDraw)
            } else {
                IntroPage(minHeight = screenHeight, compact = compact, onDraw = onDraw)
            }
        }
    }
}

// ---------- Abertura: "Vamos sortear e aprender algo agora" ----------

@Composable
private fun IntroPage(
    minHeight: Dp,
    compact: Boolean,
    onDraw: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = ContentMaxWidth)
                .fillMaxWidth()
                // Rola só se a tela for muito baixa; senão centraliza.
                .verticalScroll(rememberScrollState())
                .heightIn(min = minHeight)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CodeBadge(size = if (compact) 88.dp else 112.dp)

            Spacer(Modifier.height(28.dp))

            Text(
                text = "ESTUDE AGORA",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Vamos sortear e aprender algo agora",
                style = if (compact) MaterialTheme.typography.headlineSmall
                else MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Toque no botão e descubra um tema de programação para estudar agora.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DifficultyBadge(DifficultLearnEnum.EASY)
                DifficultyBadge(DifficultLearnEnum.MEDIUM)
                DifficultyBadge(DifficultLearnEnum.HARD)
            }

            Spacer(Modifier.height(36.dp))

            DrawButton(text = "Sortear tema", onClick = onDraw)
        }
    }
}

@Composable
private fun CodeBadge(size: Dp) {
    val accent = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(accent.copy(alpha = 0.28f), MaterialTheme.colorScheme.surface)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "</>",
            color = accent,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.36f).sp
        )
    }
}

// ---------- Resultado do sorteio (compacto: cabe na tela sem rolar) ----------

@Composable
private fun ResultPage(
    learn: Learn?,
    onDrawAgain: () -> Unit
) {
    val scrollState = rememberScrollState()
    // Volta ao topo a cada novo sorteio (só relevante se o conteúdo rolar).
    LaunchedEffect(learn?.id) { scrollState.scrollTo(0) }

    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        // Tela larga (tablet / celular deitado): duas colunas lado a lado.
        val wide = maxWidth >= WideBreakpoint

        Column(
            modifier = Modifier
                .widthIn(max = if (wide) WideMaxWidth else ContentMaxWidth)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (learn == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // A rolagem fica só como reserva para conteúdos muito longos.
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ResultTopBar(learn.difficult)

                        if (wide) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                LearnHeader(learn, Modifier.weight(1f))
                                WhatYouLearn(learn.whatLearn, Modifier.weight(1f))
                            }
                        } else {
                            LearnHeader(learn)
                            WhatYouLearn(learn.whatLearn)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            DrawButton(text = "Sortear de novo", onClick = onDrawAgain, height = 52.dp)
            Spacer(Modifier.height(8.dp))
        }
    }
}

// "SEU SORTEIO" e a dificuldade na mesma linha (economiza uma linha inteira).
@Composable
private fun ResultTopBar(difficult: DifficultLearnEnum) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SEU SORTEIO",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )
        DifficultyBadge(difficult)
    }
}

@Composable
private fun DrawButton(
    text: String,
    onClick: () -> Unit,
    height: Dp = 56.dp
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
private fun LearnHeader(learn: Learn, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = learn.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = learn.description,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Um único card com a lista numerada (menos bordas e espaçamento que um card por item).
@Composable
private fun WhatYouLearn(items: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Text(
            text = "O que você vai aprender",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        items.forEachIndexed { index, item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 21.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// ---------- Previews ----------

private val previewLearn = Learn(
    id = 1,
    title = "Two pointers: par com soma alvo em array ordenado",
    description = "Quando o array já está ordenado, dois ponteiros nas extremidades encontram um par com a soma desejada em O(n) e espaço O(1), sem precisar de hash map. É o exemplo fundamental do padrão two pointers e a base para 3Sum e 4Sum.",
    whatLearn = listOf(
        "Entender por que a ordenação permite descartar candidatos ao mover o ponteiro esquerdo ou direito",
        "Implementar o loop com left e right comparando a soma com o alvo",
        "Reconhecer quando o padrão two pointers se aplica",
        "Analisar complexidade de tempo O(n) e espaço O(1)",
        "Estender a ideia para 3Sum e 4Sum",
        "Evitar erros comuns de limites e duplicatas"
    ),
    difficult = DifficultLearnEnum.EASY
)

@Preview(showBackground = true, backgroundColor = 0xFF0E0F13, widthDp = 360, heightDp = 740)
@Composable
private fun IntroPreview() {
    EstudeAgoraTheme { LearnContent(learn = null, revealed = false, onDraw = {}) }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0F13, widthDp = 360, heightDp = 740)
@Composable
private fun ResultPreview() {
    EstudeAgoraTheme { LearnContent(learn = previewLearn, revealed = true, onDraw = {}) }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0F13, widthDp = 780, heightDp = 360)
@Composable
private fun ResultLandscapePreview() {
    EstudeAgoraTheme { LearnContent(learn = previewLearn, revealed = true, onDraw = {}) }
}
