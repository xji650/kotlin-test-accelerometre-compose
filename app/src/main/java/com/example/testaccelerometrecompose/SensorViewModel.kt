package com.example.testaccelerometrecompose

import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SensorsViewModel : ViewModel() {
    // Estado Acelerómetro
    var isColorRed by mutableStateOf(false)
    var hasAccelerometer by mutableStateOf(false)
    var accelInfo by mutableStateOf("")
    private var lastAccelUpdate: Long = 0

    // Estado Sensor de Luz
    var hasLightSensor by mutableStateOf(false)
    var lightValue by mutableStateOf(0f)
    var lightLevel by mutableStateOf("UNKNOWN")
    var maxLightRange by mutableStateOf(0f)
    var lightInfoText by mutableStateOf("No data yet")
    private var lastLightUpdate: Long = 0
    private var lastLightValue: Float = 0f

    // Method procesar Acelerómetro (con filtro de tiempo)
    fun processAccelerometer(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAccelUpdate < 1000) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val gravity = SensorManager.GRAVITY_EARTH
        val accelSqRt = (x*x + y*y + z*z) / (gravity*gravity)

        if (accelSqRt >= 2.5) { // Umbral de agitación
            isColorRed = !isColorRed
            lastAccelUpdate = currentTime
        }
    }

    // Method para procesar Luz (Filtro 1s y margen 200lx)
    fun processLightData(event: SensorEvent) {
        val currentValue = event.values[0]
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastLightUpdate > 1000 &&
            Math.abs(currentValue - lastLightValue) > 200) {

            val lowThreshold = maxLightRange / 3
            val highThreshold = (maxLightRange / 3) * 2

            lightLevel = when {
                currentValue < lowThreshold -> "LOW Intensity"
                currentValue < highThreshold -> "MEDIUM Intensity"
                else -> "HIGH Intensity"
            }

            lightValue = currentValue
            lightInfoText = "New value light sensor = $currentValue"
            lastLightValue = currentValue
            lastLightUpdate = currentTime
        }
    }
}