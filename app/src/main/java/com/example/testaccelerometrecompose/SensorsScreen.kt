package com.example.testaccelerometrecompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SensorsScreen(viewModel: SensorsViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Parte arriba (Cambia de color)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(if (viewModel.isColorRed) Color.Red else Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text("TOP AREA", color = Color.White, fontWeight = FontWeight.Bold)
        }

        // Parte centro (Mensaje e info acelerómetro)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.hasAccelerometer) {
                Text(stringResource(R.string.shake))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sensor Info: ${viewModel.accelInfo}", style = MaterialTheme.typography.bodySmall)
            } else {
                Text("Sorry, there is no accelerometer", color = Color.Red)
            }
        }

        // Parte inferior (Sensor de luz, fondo amarillo)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Yellow)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("LIGHT SENSOR", fontWeight = FontWeight.Bold)
            if (viewModel.hasLightSensor) {
                Text("Max Range: ${viewModel.maxLightRange} lx")
                Text(viewModel.lightInfoText)
                Text(viewModel.lightLevel, fontWeight = FontWeight.ExtraBold)
            } else {
                Text("Sorry, there is no light sensor")
            }
        }
    }
}