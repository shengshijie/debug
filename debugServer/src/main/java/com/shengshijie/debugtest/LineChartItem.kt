package com.shengshijie.debugtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.LineData

class LineChartItem(cd: ChartData<*>?, c: Context?) : ChartItem(cd) {
    override val itemType: Int
        get() = TYPE_LINECHART

    override fun getView(
        position: Int,
        view: View?,
        c: Context
    ): View?{
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_linechart, null
            )
            holder.chart = convertView.findViewById(R.id.chart)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.chart!!.description.isEnabled = false
        holder.chart!!.setDrawGridBackground(false)
        val xAxis = holder.chart!!.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        val leftAxis = holder.chart!!.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        val rightAxis = holder.chart!!.axisRight
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        holder.chart!!.data = mChartData as LineData
        holder.chart!!.animateX(750)
        return convertView
    }

    private class ViewHolder {
        var chart: LineChart? = null
    }
}