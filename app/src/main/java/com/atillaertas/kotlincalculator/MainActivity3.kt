package com.atillaertas.kotlincalculator

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class MainActivity3 : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var accelerometerDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometerDataTextView = findViewById(R.id.textView)

        if (accelerometer == null) {
            Toast.makeText(this, "Cihazınız ivmeölçer sensörünü desteklemiyor.", Toast.LENGTH_SHORT).show()
        } else {
            setAccelerometer()
        }
    }

    private fun setAccelerometer() {
        // Ivmeölçer verilerini alacak kodu burada yazın
        // Örneğin, sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        // gibi bir şeyler ekleyebilirsiniz.
    }

    override fun onResume() {
        super.onResume()

        accelerometer?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Değişiklikleri takip etmek için gereklidir, burada bir şey yapmanız gerekmez.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Ivmeölçer verilerini burada işleyebilirsiniz.
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val accelerometerData = "X: $x, Y: $y, Z: $z"
            accelerometerDataTextView.text = accelerometerData
        }
    }
}