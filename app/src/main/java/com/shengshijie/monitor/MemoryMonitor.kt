package com.shengshijie.monitor

import android.content.Context

class MemoryMonitor:Monitor  {
    override fun start() {}
    override fun getInfo(context: Context): String {
        return byteUnitConvert(MonitorUtils().memoryData(context).toDouble())
    }

    override fun stop() {}
}