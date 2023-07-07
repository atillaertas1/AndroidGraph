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
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.CheckBox
import com.androidplot.util.PixelUtils
import com.androidplot.util.PlotStatistics
import com.androidplot.util.Redrawer
import com.androidplot.xy.BarFormatter
import com.androidplot.xy.BarRenderer
import com.androidplot.xy.BarRenderer.Bar
import com.androidplot.xy.BoundaryMode
import com.androidplot.xy.BubbleSeries
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.StepMode
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import com.androidplot.xy.XYSeries
import java.lang.Math.pow
import java.text.DecimalFormat
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class real_time_graph : AppCompatActivity()  {
    private val HISTORY_SIZE = 100.0

    //private lateinit var sensorMgr: SensorManager
    //private lateinit var orSensor : Sensor
    private lateinit var aprLevelsPlot : XYPlot
    private lateinit var aprHistoryPlot : XYPlot

    private lateinit var hwAcceleratedCb : CheckBox
    private lateinit var showFpsCb : CheckBox

    private lateinit var aLvlSeries : SimpleXYSeries
    private lateinit var pLvlSeries: SimpleXYSeries
    private lateinit var rLvlSeries: SimpleXYSeries

    private lateinit var azimuthHistorySeries : SimpleXYSeries
    private lateinit var pitchHistorySeries : SimpleXYSeries
    private lateinit var rollHistorySeries : SimpleXYSeries
    private lateinit var sinusHistorySeries : SimpleXYSeries

    private lateinit var aLvlSeriesColor : BarFormatter
    private lateinit var pLvlSeriesColor : BarFormatter
    private lateinit var rLvlSeriesColor : BarFormatter

    private lateinit var azimuthHistorySeriesColor : LineAndPointFormatter
    private lateinit var pitchHistorySeriesColor : LineAndPointFormatter
    private lateinit var rollHistorySeriesColor : LineAndPointFormatter
    private lateinit var sinusHistorySeriesColor : LineAndPointFormatter

    private lateinit var redrawer: Redrawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_time_graph)

        var updateButton: Button = findViewById(R.id.updateButton)
        var stopButton: Button = findViewById(R.id.stopButton)
        var startButton: Button = findViewById(R.id.startButton)
        var graphIsFinish: Boolean = false
        var updateButtonIsActive = true
        //var graphController : Boolean = true

        // setup the APR Levels plot
        aprLevelsPlot = findViewById(R.id.aprLevelsPlot)
        aprLevelsPlot.setDomainBoundaries(-2, 2, BoundaryMode.FIXED)

        aLvlSeries = SimpleXYSeries("A")
        pLvlSeries = SimpleXYSeries("P")
        rLvlSeries = SimpleXYSeries("R")

        aLvlSeriesColor = BarFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 80, 0))
        pLvlSeriesColor = BarFormatter(Color.rgb(200, 0, 0), Color.rgb(0, 80, 0))
        rLvlSeriesColor = BarFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 80, 0))

        aprLevelsPlot.addSeries(aLvlSeries, aLvlSeriesColor)
        aprLevelsPlot.addSeries(pLvlSeries, pLvlSeriesColor)
        aprLevelsPlot.addSeries(rLvlSeries, rLvlSeriesColor)

        aprLevelsPlot.domainStepValue = 3.0
        aprLevelsPlot.linesPerRangeLabel = 3

        // per the android documentation, the minimum and maximum readings we can get from
        // any of the orientation sensors is -180 and 359 respectively so we will fix our plot's
        // boundaries to those values.  If we did not do this, the plot would auto-range which
        // can be visually confusing in the case of dynamic plots.

        aprLevelsPlot.setRangeBoundaries(-1, 1, BoundaryMode.FIXED)

        // update our domain and range axis labels:
        aprLevelsPlot.setDomainLabel("")
        aprLevelsPlot.domainTitle.pack()
        aprLevelsPlot.setRangeLabel("Angle (Degs)")
        aprLevelsPlot.rangeTitle.pack()
        aprLevelsPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#")


        // setup the APR History plot:

        aprHistoryPlot = findViewById(R.id.aprHistoryPlot)

        azimuthHistorySeries = SimpleXYSeries("Az.")
        azimuthHistorySeries.useImplicitXVals()
        pitchHistorySeries = SimpleXYSeries("Pitch")
        pitchHistorySeries.useImplicitXVals()
        rollHistorySeries = SimpleXYSeries("Roll")
        rollHistorySeries.useImplicitXVals()
        sinusHistorySeries = SimpleXYSeries("Sinus")
        sinusHistorySeries.useImplicitXVals()

        azimuthHistorySeriesColor =
            LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null)
        pitchHistorySeriesColor = LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null)
        rollHistorySeriesColor = LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null)
        sinusHistorySeriesColor = LineAndPointFormatter(Color.rgb(200, 50, 100), null, null, null)

        aprHistoryPlot.setRangeBoundaries(-2, 2, BoundaryMode.FIXED)
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED)
        aprHistoryPlot.addSeries(azimuthHistorySeries, azimuthHistorySeriesColor)
        aprHistoryPlot.addSeries(pitchHistorySeries, pitchHistorySeriesColor)
        aprHistoryPlot.addSeries(rollHistorySeries, rollHistorySeriesColor)
        aprHistoryPlot.addSeries(sinusHistorySeries, sinusHistorySeriesColor)
        aprHistoryPlot.domainStepMode = StepMode.INCREMENT_BY_VAL
        aprHistoryPlot.domainStepValue = HISTORY_SIZE / 10
        aprHistoryPlot.linesPerRangeLabel = 3
        aprHistoryPlot.setDomainLabel("SampleIndex")
        aprHistoryPlot.domainTitle.pack()
        aprHistoryPlot.setRangeLabel("Angle (Degs)")
        aprHistoryPlot.rangeTitle.pack()

        aprHistoryPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#")
        aprHistoryPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format =
            DecimalFormat("#")

        // setup checkboxes:

        hwAcceleratedCb = findViewById(R.id.hwAccelerationCb)

        val levelStats = PlotStatistics(1000, false)
        val histStats = PlotStatistics(1000, false)

        aprLevelsPlot.addListener(levelStats)
        aprHistoryPlot.addListener(histStats)

        hwAcceleratedCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                aprLevelsPlot.setLayerType(View.LAYER_TYPE_NONE, null)
                aprHistoryPlot.setLayerType(View.LAYER_TYPE_NONE, null)
            } else {
                aprLevelsPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                aprHistoryPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
        }

        showFpsCb = findViewById(R.id.showFpsCb)

        showFpsCb.setOnCheckedChangeListener { compoundButton, b ->
            levelStats.setAnnotatePlotEnabled(b)
            histStats.setAnnotatePlotEnabled(b)
        }

        // get a ref to the BarRenderer so we can make some changes to it:

        val barRenderer: BarRenderer<BarFormatter> =
            aprLevelsPlot.getRenderer(BarRenderer::class.java) as BarRenderer<BarFormatter>

        if (barRenderer != null) {
            barRenderer.setBarGroupWidth(
                BarRenderer.BarGroupWidthMode.FIXED_WIDTH,
                PixelUtils.dpToPix(
                    18F
                )
            )
        }

        // register for orientation sensor events:
        /*
        sensorMgr = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        for (sensor in sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER)){ /* sensor degisim !*/
            if (sensor.type == Sensor.TYPE_ACCELEROMETER){
                orSensor = sensor
            }
        }

        if (orSensor == null) {
            System.out.println("HATA!")
            Log.d("orSensor-Error","orSensor Hatasi")
            cleanUp()
        }

        sensorMgr.registerListener(this,orSensor,SensorManager.SENSOR_DELAY_UI)
*/
        redrawer = Redrawer(
            listOf(aprHistoryPlot, aprLevelsPlot),
            33.3f,
            true
        )


        updateButton.setOnClickListener {
            if (updateButtonIsActive) {
                Thread {
                    while (true) {
                        val sinusList = generateSinusoidalData(1.0, 1.0, 0.0, 10)

                        if (sinusHistorySeries.size() > HISTORY_SIZE) {
                            val numToRemove = sinusHistorySeries.size() - HISTORY_SIZE

                            for (i in 0 until numToRemove.toInt()) {
                                sinusHistorySeries.removeFirst()
                            }
                        }

                        for (i in sinusList) {
                            sinusHistorySeries.addLast(null, i)
                        }
                    }
                }.start()
                updateButtonIsActive = false
            }
        }

        stopButton.setOnClickListener {
            redrawer.finish()
            graphIsFinish = true
        }

        startButton.setOnClickListener {
            if (graphIsFinish) {
                redrawer = Redrawer(
                    listOf(aprHistoryPlot, aprLevelsPlot),
                    33.3f,
                    true
                )
                redrawer.start()
            }
        }

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @Override
    public override fun onResume() {
        super.onResume()
        redrawer.start()

        Log.d("deneme","deneme")

    }

    @Override
    public override fun onPause() {
        redrawer.pause()
        super.onPause()
    }

    override fun onDestroy() {
        redrawer.finish()
        super.onDestroy()
    }
/*
    private fun cleanUp() {
        // aunregister with the orientation sensor before exiting:
        sensorMgr.unregisterListener(this)
        finish()
    }
*//*
    override fun onSensorChanged(sensorEvent: SensorEvent?) {



        /*
        aLvlSeries.setModel(
            listOf(sensorEvent?.values?.get(0)),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY
        )

        pLvlSeries.setModel(
            listOf(sensorEvent?.values?.get(1)),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY
        )

        rLvlSeries.setModel(
            listOf(sensorEvent?.values?.get(2)),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY
        )
        */

        // get rid the oldest sample in history:
       /* if (rollHistorySeries.size() > HISTORY_SIZE) {
            rollHistorySeries.removeFirst()
            pitchHistorySeries.removeFirst()
            azimuthHistorySeries.removeFirst()

        }*/



        // add the latest history sample:

        //azimuthHistorySeries.addLast(null,sensorEvent?.values?.get(0))
        //pitchHistorySeries.addLast(null,sensorEvent?.values?.get(1))
        //rollHistorySeries.addLast(null,sensorEvent?.values?.get(2))










        //angle
/*
        var x : Double = sensorEvent?.values?.get(0)?.toDouble() ?: 0.0
        var y : Double = sensorEvent?.values?.get(1)?.toDouble() ?: 0.0
        var z : Double = sensorEvent?.values?.get(2)?.toDouble() ?: 0.0

        var pitch : Double = Math.atan(x/Math.sqrt(Math.pow(y,2.0) + Math.pow(z,2.0)))
        var roll : Double = Math.atan(y/Math.sqrt(Math.pow(x,2.0) + Math.pow(z,2.0)))

        pitch = pitch * (180 / Math.PI)
        roll = roll * (180 / Math.PI)

        Log.d("pitch-roll","$pitch <=> $roll")

*/
    }
*//*
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }*/
    fun generateSinusoidalData(
        amplitude: Double,
        frequency: Double,
        phaseShift: Double,
        numPoints: Int
    ): List<Number?> {
        val data = mutableListOf<Number?>()

        val stepSize = (2 * Math.PI) / numPoints
        var x = 0.0

        for (i in 0 until numPoints) {
            val y = amplitude * sin(frequency * x + phaseShift)
            data.add(y)

            x += stepSize
        }

        return data
    }




}