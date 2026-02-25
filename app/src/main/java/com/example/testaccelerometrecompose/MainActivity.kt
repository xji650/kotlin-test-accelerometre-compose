package com.example.testaccelerometrecompose

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.testaccelerometrecompose.ui.theme.TestAccelerometreComposeTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val viewModel: SensorsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Punto 1: Verificar disponibilidad y capacidades
        val acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        viewModel.hasAccelerometer = acc != null
        viewModel.hasLightSensor = light != null

        acc?.let { viewModel.accelInfo = "Vendor: ${it.vendor}, Power: ${it.power}mA" }
        light?.let { viewModel.maxLightRange = it.maximumRange }

        setContent {
            TestAccelerometreComposeTheme {
                Scaffold { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        SensorsScreen(viewModel)
                    }
                }
            }
        }
    }

    // Punto 2 y 8: Registrar en onResume para garantizar reactivación
    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Punto 2 y 8: Desregistrar en onPause para ahorrar batería
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> viewModel.processAccelerometer(event)
            Sensor.TYPE_LIGHT -> viewModel.processLightData(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}



