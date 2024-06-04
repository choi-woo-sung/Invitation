//package com.woosung.invitation
//
//fun init() {
//    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
//    val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//
//    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
//    sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
//}
//
//val data: Channel<SensorData> = Channel(Channel.UNLIMITED)
//
//override fun onSensorChanged(event: SensorEvent?) {
//    ..
//    data.trySend(
//        SensorData(
//            roll = orientation[2],
//            pitch = orientation[1]
//        )
//    )
//}
//
//data class SensorData(
//    val roll: Float,
//    val pitch: Float
//)