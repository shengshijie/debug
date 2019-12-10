package com.shengshijie.debugtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ChartData

class BarChartItem(cd: ChartData<*>?, c: Context?) : ChartItem(cd) {
    override val itemType: Int
        get() = TYPE_BARCHART

    override fun getView(
        position: Int,
        view: View?,
        c: Context
    ): View? {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(c).inflate(R.layout.list_item_barchart, null)
            holder.chart = convertView.findViewById(R.id.chart)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.chart!!.description.isEnabled = false
        holder.chart!!.setDrawGridBackground(false)
        holder.chart!!.setDrawBarShadow(false)
        val xAxis = holder.chart!!.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        val leftAxis = holder.chart!!.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.spaceTop = 20f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        val rightAxis = holder.chart!!.axisRight
        rightAxis.setLabelCount(5, false)
        rightAxis.spaceTop = 20f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        holder.chart!!.data = mChartData as BarData
        holder.chart!!.setFitBars(true)
        holder.chart!!.animateY(700)
        return convertView
    }

    private class ViewHolder {
        var chart: BarChart? = null
    }
}