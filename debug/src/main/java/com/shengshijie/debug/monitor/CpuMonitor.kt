package com.shengshijie.debug.monitor

import android.content.Context

class CpuMonitor:Monitor {
    override fun start() {}
    override fun getInfo(context: Context): String {
        return MonitorUtils.cpuPercent().toString()
    }

    override fun stop() {}
}