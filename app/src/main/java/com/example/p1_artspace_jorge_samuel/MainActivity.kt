/*
 * Participantes del grupo:
 * - Jorge Garre
 * - Samuel Parra
 * Proyecto: P1_ArtSpace_Jorge_Samuel
 */

package com.example.p1_artspace_jorge_samuel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class FotoGaleria(
    @param:DrawableRes val imageRes: Int,
    val titulo: String,
    val anio: Int
)

private val LightGaleriaColors = lightColorScheme(
    primary = Color(0xFF0F4C81),
    onPrimary = Color.White,
    secondary = Color(0xFF00838F),
    onSecondary = Color.White,
    background = Color(0xFFF7F8FA),
    onBackground = Color(0xFF14171A),
    surface = Color.White,
    onSurface = Color(0xFF14171A),
    outline = Color(0xFF1E5AA8)
)

private val DarkGaleriaColors = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF0A1929),
    secondary = Color(0xFF4DD0E1),
    onSecondary = Color(0xFF062024),
    background = Color(0xFF101418),
    onBackground = Color(0xFFECEFF1),
    surface = Color(0xFF1A2027),
    onSurface = Color(0xFFECEFF1),
    outline = Color(0xFF90CAF9)
)

@Composable
fun GaleriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkGaleriaColors else LightGaleriaColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GaleriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GaleriaFotosApp()
                }
            }
        }
    }
}

@Composable
fun GaleriaFotosApp() {
    // Imágenes incorporadas en res/drawable desde "fotos moviles"
    val fotos = remember {
        listOf(
            FotoGaleria(R.drawable.calajo, "Calajo, el gato elegante", 2021),
            FotoGaleria(R.drawable.hamsterhappy, "Hamster arcoíris", 2022),
            FotoGaleria(R.drawable.patricio, "Patricio soñador", 2023),
            FotoGaleria(R.drawable.placton, "Plankton melancólico", 2020),
            FotoGaleria(R.drawable.po, "El guerrero drogón", 2024),
            FotoGaleria(R.drawable.reyjulien, "Mauricio no se mueve", 2025)
        )
    }

    // 1. Cuando inicie la aplicación saldrá la primera imagen (índice 0)
    var indiceActual by remember { mutableIntStateOf(0) }
    val totalFotos = fotos.size
    val fotoActual = fotos[indiceActual]

    // Acciones cíclicas y de extremos
    val irPrimera = { indiceActual = 0 }
    val irUltima = { indiceActual = totalFotos - 1 }
    val irAnterior = {
        indiceActual = if (indiceActual == 0) totalFotos - 1 else indiceActual - 1
    }
    val irSiguiente = {
        indiceActual = if (indiceActual == totalFotos - 1) 0 else indiceActual + 1
    }

    // Adaptación móvil / tablet según ancho de pantalla
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val maxContentWidth = if (isTablet) 720.dp else 480.dp
    val imageHeight = if (isTablet) 420.dp else 280.dp
    val outerPadding = if (isTablet) 28.dp else 16.dp

    // En móvil la imagen queda centrada verticalmente y el resto va debajo
    val espacioSuperior = if (isTablet) {
        0.dp
    } else {
        ((configuration.screenHeightDp - imageHeight.value) / 2f - 130f)
            .coerceAtLeast(0f).dp
    }
    val espacioInferior = if (isTablet) 20.dp else 32.dp

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(outerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxContentWidth),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Barra de título
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "GALERIA Jorge y Samuel",
                    style = if (isTablet) {
                        MaterialTheme.typography.headlineMedium
                    } else {
                        MaterialTheme.typography.titleLarge
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(espacioSuperior))

            // 2. Galería fotográfica
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = fotoActual.imageRes),
                    contentDescription = "${fotoActual.titulo} (${fotoActual.anio})",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.height(espacioInferior))

            // 3. Título de la foto y 4. numero de imagen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${fotoActual.titulo} (${fotoActual.anio})",
                    style = if (isTablet) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        MaterialTheme.typography.titleMedium
                    },
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Imagen ${indiceActual + 1}/$totalFotos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(espacioInferior))

            // 5. Botones: Primera, Anterior, Siguiente, Última
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BotonGaleria("Primera", irPrimera)
                        BotonGaleria("Anterior", irAnterior)
                        BotonGaleria("Siguiente", irSiguiente)
                        BotonGaleria("Última", irUltima)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BotonGaleria("Primera", irPrimera, Modifier.weight(1f), compact = true)
                        BotonGaleria("Anterior", irAnterior, Modifier.weight(1f), compact = true)
                        BotonGaleria("Siguiente", irSiguiente, Modifier.weight(1f), compact = true)
                        BotonGaleria("Última", irUltima, Modifier.weight(1f), compact = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonGaleria(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        contentPadding = if (compact) {
            PaddingValues(horizontal = 4.dp, vertical = 10.dp)
        } else {
            ButtonDefaults.ContentPadding
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = texto,
            style = if (compact) {
                MaterialTheme.typography.labelSmall
            } else {
                MaterialTheme.typography.labelLarge
            },
            maxLines = 1
        )
    }
}
