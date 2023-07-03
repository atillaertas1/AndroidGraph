package com.atillaertas.kotlincalculator

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import com.androidplot.util.PixelUtils
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.XYSeries
import com.androidplot.xy.CatmullRomInterpolator
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition


class MainActivity2 : Activity() {
    private lateinit var plot: XYPlot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_xy_plot_example)

        // initialize our XYPlot reference:
        plot = findViewById(R.id.plot)

        // create a couple arrays of y-values to plot:
        val domainLabels = arrayOf<Number>(1, 2, 3, 6, 7, 8, 9, 10, 13, 14)
        val series1Numbers = arrayOf<Number>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val series2Numbers = arrayOf<Number>(5, 10, 15, 10, 5, 10, 20, 40, 80, 120)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        val series1: XYSeries = SimpleXYSeries(
            listOf(*series1Numbers),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
            "Series1"
        )
        val series2: XYSeries = SimpleXYSeries(
            listOf(*series2Numbers),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
            "Series2"
        )

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)
        val series2Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2)

        // add an "dash" effect to the series2 line:
        series2Format.linePaint.pathEffect = DashPathEffect(
            floatArrayOf(
                PixelUtils.dpToPix(2f),
                PixelUtils.dpToPix(5f)
            ), 0f
        )

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        /*series1Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)
        series2Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)*/

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format)
        plot.addSeries(series2, series2Format)

        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM)
            .format = object : Format() {
            override fun format(
                obj: Any,
                toAppendTo: StringBuffer,
                pos: FieldPosition
            ): StringBuffer {
                val i = (obj as Number).toFloat().toInt()
                return toAppendTo.append(domainLabels[i])
            }

            override fun parseObject(source: String, pos: ParsePosition): Any? {
                return null
            }
        }
    }
}
