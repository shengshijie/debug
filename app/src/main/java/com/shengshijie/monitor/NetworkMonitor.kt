package com.shengshijie.monitor

import android.content.Context
import android.net.TrafficStats

class NetworkMonitor:Monitor  {

    private var total: Long = 0
    internal var interval: Int = 1

    override fun start() {

    }

    override fun getInfo(context: Context): String {
        val sum = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()
        val last = sum - total
        total = sum
        return byteUnitConvert((last / interval).toDouble()) + "/S"
    }

    override fun stop() {}

}