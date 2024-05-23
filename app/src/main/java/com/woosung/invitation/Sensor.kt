package com.woosung.invitation

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

//https://medium.com/@rahulbehera/sensor-based-parallax-animations-for-android-and-ios-5363f38e37ec
@Composable
fun observeDeviceRotation(): RotationState {

    val context = LocalContext.current
    val sm = context.getSystemService(SENSOR_SERVICE) as SensorManager
    val sensitivity = 90f // adjust for sensitivity

    val initial = FloatArray(9) // first rotation matrix from sensor
    val current = FloatArray(9) // most current rotation matrix from sensor
    var hasCapturedInitialEvent = false

    val rotationState = remember { mutableStateOf(RotationState(pitch = 0f, roll = 0f)) }

    DisposableEffect(Unit) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (hasCapturedInitialEvent) {
//                    SensorManager.getAngleChange()
                    SensorManager.getRotationMatrixFromVector(current, event.values)
                    SensorManager.
                    // Inspired by SensorManger.getAngleChange
                    val pitch = -(initial[2] * current[0]
                            + initial[5] * current[3]
                            + initial[8] * current[6]) * sensitivity
                    val roll = -(initial[2] * current[1]
                            + initial[5] * current[4]
                            + initial[8] * current[7]) * sensitivity

                    rotationState.value = RotationState(roll, pitch)
                } else {
                    SensorManager.getRotationMatrixFromVector(initial, event.values)
                    hasCapturedInitialEvent = true
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Handle accuracy change
            }
        }

        sm.registerListener(
            sensorEventListener,
            sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sm.unregisterListener(sensorEventListener)
        }
    }
    return rotationState.value
}

data class RotationState(
    val pitch: Float,
    val roll: Float,
)
