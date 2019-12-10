package com.shengshijie.debugtest

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.shengshijie.debug.monitor.MonitorBean
import com.shengshijie.debug.monitor.byteUnitParse
import com.shengshijie.debug.upload.Server
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        HLog.setLogImpl(LogbackImpl().apply {
            file = true
            db = true
        })
        HLog.init(application, getExternalFilesDir(null)?.absolutePath, "RFT")

        lc_content.setDrawGridBackground(false)
        lc_content.description.isEnabled = false
        lc_content.setNoDataText("No data available")
        lc_content.invalidate()
        thread {
            Server.start(8088) {
                try {
                    val monitorInfo = Gson().fromJson(it, MonitorBean::class.java)
                    HLog.string(monitorInfo)
                    addEntry(monitorInfo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun addEntry(monitorInfo: MonitorBean) {
        var data: LineData? = lc_content.data
        if (data == null) {
            data = LineData()
            lc_content.data = data
        }
        var set = data.getDataSetByIndex(0)
        if (set == null) {
            set = LineDataSet(null, "DataSet 1")
            set.lineWidth = 2.5f
            set.circleRadius = 4.5f
            set.color = Color.rgb(240, 99, 99)
            set.setCircleColor(Color.rgb(240, 99, 99))
            set.highLightColor = Color.rgb(190, 190, 190)
            set.axisDependency = AxisDependency.LEFT
            set.valueTextSize = 10f
            data.addDataSet(set)
        }
        val randomDataSetIndex = (Math.random() * data.dataSetCount).toInt()
        val randomSet = data.getDataSetByIndex(randomDataSetIndex)
        val value = byteUnitParse(monitorInfo.networkmonitor)
        data.addEntry(Entry(randomSet.entryCount.toFloat(), value.toFloat()), randomDataSetIndex)
        data.notifyDataChanged()
        lc_content.notifyDataSetChanged()
        lc_content.setVisibleXRangeMaximum(6f)
        lc_content.moveViewTo(data.entryCount - 7.toFloat(), 50f, AxisDependency.LEFT)
    }

}

