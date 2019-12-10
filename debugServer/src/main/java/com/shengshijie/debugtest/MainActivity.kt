package com.shengshijie.debugtest

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.shengshijie.debug.monitor.MonitorBean
import com.shengshijie.debug.monitor.memParse
import com.shengshijie.debug.monitor.netParse
import com.shengshijie.debug.upload.Server
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.setLogImpl(LogbackImpl().apply {
            file = true
            db = true
        })
        HLog.init(application, getExternalFilesDir(null)?.absolutePath, "RFT")
        thread {
            Server.start(8088) {
                try {
                    val monitorInfo = Gson().fromJson(it, MonitorBean::class.java)
                    HLog.string(monitorInfo)
                    addEntry(lc_cpu, "CPU", monitorInfo.cpumonitor.toFloat())
                    addEntry(lc_memory, "MEMORY", memParse(monitorInfo.memorymonitor).toFloat())
                    addEntry(lc_network, "NETWORK", netParse(monitorInfo.networkmonitor).toFloat())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun addEntry(lineChart: LineChart, name: String, monitorData: Float) {
        var data: LineData? = lineChart.data
        if (data == null) {
            data = LineData()
            lineChart.data = data
        }
        var set = data.getDataSetByIndex(0)
        if (set == null) {
            set = LineDataSet(null, name)
            set.lineWidth = 2.5f
            set.circleRadius = 4.5f
            set.color = Color.rgb(240, 99, 99)
            set.setCircleColor(Color.rgb(240, 99, 99))
            set.highLightColor = Color.rgb(190, 190, 190)
            set.axisDependency = AxisDependency.LEFT
            set.valueTextSize = 10f
            data.addDataSet(set)
        }
        data.addEntry(Entry(data.getDataSetByIndex(0).entryCount.toFloat(), monitorData), 0)
        data.notifyDataChanged()
        lineChart.notifyDataSetChanged()
        lineChart.setVisibleXRangeMaximum(6f)
        lineChart.moveViewTo(data.entryCount - 7.toFloat(), 50f, AxisDependency.LEFT)
    }

}

