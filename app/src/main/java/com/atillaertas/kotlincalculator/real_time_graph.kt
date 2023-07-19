package com.atillaertas.kotlincalculator

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import com.androidplot.util.PixelUtils
import com.androidplot.util.PlotStatistics
import com.androidplot.util.Redrawer
import com.androidplot.xy.BarFormatter
import com.androidplot.xy.BarRenderer
import com.androidplot.xy.BoundaryMode
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.Step
import com.androidplot.xy.StepMode
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import com.atillaertas.kotlincalculator.serial.Sending01010101Activity
import com.atillaertas.kotlincalculator.serial.SerialMainMenu
import java.text.DecimalFormat
import kotlin.math.sin

class real_time_graph : AppCompatActivity()  {
    private val HISTORY_SIZE =80.0
    private val HISTORY_SIZE_2 = 100.0
    private val HISTORY_SIZE_3 = 120.0

    private lateinit var aprHistoryPlot : XYPlot
    private lateinit var aprHistory_2_Plot : XYPlot
    private lateinit var aprHistory_3_Plot : XYPlot
    private lateinit var hwAcceleratedCb : CheckBox
    private lateinit var showFpsCb : CheckBox
    private lateinit var sinusHistorySeries : SimpleXYSeries
    private lateinit var sinusHistory_2_Series : SimpleXYSeries
    private lateinit var sinusHistory_3_Series : SimpleXYSeries
    private lateinit var sinusHistorySeriesColor : LineAndPointFormatter
    private lateinit var sinusHistory_2_SeriesColor : LineAndPointFormatter
    private lateinit var sinusHistory_3_SeriesColor : LineAndPointFormatter
    private lateinit var redrawer: Redrawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_time_graph)

        var startButton: Button = findViewById(R.id.startButton)
        var stopButton: Button = findViewById(R.id.stopButton)
        var contunieButton: Button = findViewById(R.id.contunieButton)
        var sendButton: Button = findViewById(R.id.sendButton)
        var graphIsFinish: Boolean = false
        var startButtonIsActive = true

        aprHistoryPlot = findViewById(R.id.aprHistoryPlot)
        aprHistory_2_Plot = findViewById(R.id.aprHistory_2_Plot)
        aprHistory_3_Plot = findViewById(R.id.aprHistory_3_Plot)

        sinusHistorySeries = SimpleXYSeries("Sinus")
        sinusHistorySeries.useImplicitXVals()
        sinusHistory_2_Series = SimpleXYSeries("Sinus 2")
        sinusHistory_2_Series.useImplicitXVals()
        sinusHistory_3_Series = SimpleXYSeries("Sinus 3")
        sinusHistory_3_Series.useImplicitXVals()

        sinusHistorySeriesColor = LineAndPointFormatter(Color.rgb(200, 50, 100), null, null, null)
        sinusHistory_2_SeriesColor = LineAndPointFormatter(Color.rgb(50, 200, 100), null, null, null)
        sinusHistory_3_SeriesColor = LineAndPointFormatter(Color.rgb(155, 10, 10), null, null, null)

        aprHistory_3_Plot.setDomainBoundaries(-2,2,BoundaryMode.FIXED)
        aprHistory_3_Plot.setDomainBoundaries(0,HISTORY_SIZE_3,BoundaryMode.FIXED)
        aprHistory_3_Plot.addSeries(sinusHistory_3_Series,sinusHistory_3_SeriesColor)
        aprHistory_3_Plot.domainStepMode = StepMode.INCREMENT_BY_VAL
        aprHistory_3_Plot.domainStepValue = HISTORY_SIZE_3 / 10
        aprHistory_3_Plot.linesPerRangeLabel = 100
        aprHistory_3_Plot.setDomainLabel("Sinus - III")
        aprHistory_3_Plot.domainTitle.pack()
        aprHistory_3_Plot.setRangeLabel("")
        aprHistory_3_Plot.rangeTitle.pack()

        aprHistory_3_Plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#")
        aprHistory_3_Plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")


        aprHistory_2_Plot.setDomainBoundaries(-2,2,BoundaryMode.FIXED)
        aprHistory_2_Plot.setDomainBoundaries(0,HISTORY_SIZE_2,BoundaryMode.FIXED)
        aprHistory_2_Plot.addSeries(sinusHistory_2_Series,sinusHistory_2_SeriesColor)
        aprHistory_2_Plot.domainStepMode = StepMode.INCREMENT_BY_VAL
        aprHistory_2_Plot.domainStepValue = HISTORY_SIZE_2 / 10
        aprHistory_2_Plot.linesPerRangeLabel = 100
        aprHistory_2_Plot.setDomainLabel("Sinus - II")
        aprHistory_2_Plot.domainTitle.pack()
        aprHistory_2_Plot.setRangeLabel("")
        aprHistory_2_Plot.rangeTitle.pack()

        aprHistory_2_Plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#")
        aprHistory_2_Plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")


        aprHistoryPlot.setRangeBoundaries(-2, 2, BoundaryMode.FIXED)    // y ekseni
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED)     // x ekseni
        aprHistoryPlot.addSeries(sinusHistorySeries, sinusHistorySeriesColor)
        //aprHistoryPlot.addSeries(sinusHistory_2_Series, sinusHistory_2_SeriesColor)
        //aprHistoryPlot.addSeries(sinusHistory_3_Series, sinusHistory_3_SeriesColor)
        aprHistoryPlot.domainStepMode = StepMode.INCREMENT_BY_VAL
        aprHistoryPlot.domainStepValue = HISTORY_SIZE / 10
        aprHistoryPlot.linesPerRangeLabel = 100
        aprHistoryPlot.setDomainLabel("Sinus - I")
        aprHistoryPlot.domainTitle.pack()
        aprHistoryPlot.setRangeLabel("")
        aprHistoryPlot.rangeTitle.pack()

        aprHistoryPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#")
        aprHistoryPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")

        // setup checkboxes:

        hwAcceleratedCb = findViewById(R.id.hwAccelerationCb)

        val histStats = PlotStatistics(1000, false)
        val histStats_2 = PlotStatistics(1000,false)
        val histStats_3 = PlotStatistics(1000,false)

        aprHistoryPlot.addListener(histStats)
        aprHistory_2_Plot.addListener(histStats_2)
        aprHistory_3_Plot.addListener(histStats_3)

        hwAcceleratedCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                aprHistoryPlot.setLayerType(View.LAYER_TYPE_NONE, null)
                aprHistory_2_Plot.setLayerType(View.LAYER_TYPE_NONE,null)
                aprHistory_3_Plot.setLayerType(View.LAYER_TYPE_NONE,null)
            } else {
                aprHistoryPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                aprHistory_2_Plot.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
                aprHistory_3_Plot.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
            }
        }

        showFpsCb = findViewById(R.id.showFpsCb)

        showFpsCb.setOnCheckedChangeListener { compoundButton, b ->
            histStats.setAnnotatePlotEnabled(b)
            histStats_2.setAnnotatePlotEnabled(b)
            histStats_3.setAnnotatePlotEnabled(b)
        }

        redrawer = Redrawer(
            listOf(aprHistoryPlot,aprHistory_2_Plot,aprHistory_3_Plot),
            33.3f,
            false
        )


        startButton.setOnClickListener {
            if (startButtonIsActive) {
                Thread {
                    var index = 0
                    while (true) {
                        val sinusList = generateSinusoidalData(0.5, 1.0, 0.0, 5)

                        if (sinusHistorySeries.size() > HISTORY_SIZE) {
                            sinusHistorySeries.removeFirst()
                        }


                        // elemanlari tek tek gönderiyoruz ki performans sorunları olmasın
                        val element = sinusList.getOrNull(index)

                        if (element != null) {
                            sinusHistorySeries.addLast(null, element)
                            Thread.sleep(100)
                        }

                        index++

                        if (index >= sinusList.size){
                            index = 0
                        }


                    }
                }.start()

                Thread{
                    var index = 0
                    while (true){

                        val sinusList_2 = generateSinusoidalData(0.5,0.5,0.0,5)

                        if (sinusHistory_2_Series.size() > HISTORY_SIZE_2){
                            sinusHistory_2_Series.removeFirst()
                        }

                        val element_2 = sinusList_2.getOrNull(index)

                        if (element_2 != null){
                            sinusHistory_2_Series.addLast(null,element_2)
                            Thread.sleep(5)
                        }

                        index++

                        if (index >= sinusList_2.size){
                            index = 0
                        }
                    }
                }.start()

                Thread{
                    var index = 0
                    while (true) {
                        val sinusList_3 = generateSinusoidalData(0.5,0.5,0.0,5)

                        if (sinusHistory_3_Series.size() > HISTORY_SIZE_3) {
                            sinusHistory_3_Series.removeFirst()
                        }

                        val element_3 = sinusList_3.getOrNull(index)


                        if (element_3 != null){
                            sinusHistory_3_Series.addLast(null,element_3)
                            Thread.sleep(30)
                        }

                        Log.d("data-3","${sinusHistorySeries.size()}")

                        index++

                        if (index >= sinusList_3.size){
                            index = 0
                        }
                    }
                }.start()

                startButtonIsActive = false
            }
        }

        stopButton.setOnClickListener {
            redrawer.finish()
            graphIsFinish = true
        }

        contunieButton.setOnClickListener {
            if (graphIsFinish) {
                redrawer = Redrawer(
                    listOf(aprHistoryPlot,aprHistory_2_Plot,aprHistory_3_Plot),
                    33.3f,
                    true
                )
                redrawer.start()
            }
        }

        sendButton.setOnClickListener {
            val intent = Intent(this,SerialMainMenu::class.java)
            startActivity(intent)
        }

    }

    @Override
    public override fun onResume() {
        super.onResume()
        redrawer.start()
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