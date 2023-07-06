package com.atillaertas.kotlincalculator

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.androidplot.xy.BoundaryMode
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.StepMode
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import java.text.DecimalFormat

class MainActivity3 : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var accelerometerPlot: XYPlot

    private lateinit var xSeries: SimpleXYSeries
    private lateinit var ySeries: SimpleXYSeries
    private lateinit var zSeries: SimpleXYSeries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        accelerometerPlot = findViewById(R.id.accelerometerPlot)

        xSeries = SimpleXYSeries("X")
        ySeries = SimpleXYSeries("Y")
        zSeries = SimpleXYSeries("Z")

        accelerometerPlot.setBackgroundColor(Color.WHITE)
        accelerometerPlot.setDomainLabel("time")
        accelerometerPlot.setRangeLabel("value")

        val seriesFormatter = LineAndPointFormatter(
            Color.WHITE, null, null, null
        )
        val seriesFormatter2 = LineAndPointFormatter(
            Color.GREEN, null, null, null
        )
        val seriesFormatter3 = LineAndPointFormatter(
            Color.BLUE, null, null, null
        )

        accelerometerPlot.addSeries(xSeries, seriesFormatter)
        accelerometerPlot.addSeries(ySeries, seriesFormatter2)
        accelerometerPlot.addSeries(zSeries, seriesFormatter3)

        accelerometerPlot.setRangeBoundaries(-10, 10, BoundaryMode.FIXED)
        accelerometerPlot.setDomainBoundaries(0, 100, BoundaryMode.FIXED)
        accelerometerPlot.domainStepMode = StepMode.INCREMENT_BY_VAL
        accelerometerPlot.domainStepValue = 10.0

        accelerometerPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT)
            .format = DecimalFormat("#")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
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
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            xSeries.addLast(null, x)
            ySeries.addLast(null, y)
            zSeries.addLast(null, z)

            if (xSeries.size() > 100) {
                xSeries.removeFirst()
                ySeries.removeFirst()
                zSeries.removeFirst()
            }

            accelerometerPlot.redraw()
            Log.d("data", "$x,$y,$z")
        }
    }
}