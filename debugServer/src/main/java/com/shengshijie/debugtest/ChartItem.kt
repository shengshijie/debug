package com.shengshijie.debugtest

import android.content.Context
import android.view.View
import com.github.mikephil.charting.data.ChartData

abstract class ChartItem internal constructor(var mChartData: ChartData<*>?) {

    abstract val itemType: Int

    abstract fun getView(position: Int, convertView: View?, c: Context): View?

    companion object {
        const val TYPE_BARCHART = 0
        const val TYPE_LINECHART = 1
        const val TYPE_PIECHART = 2
    }

}