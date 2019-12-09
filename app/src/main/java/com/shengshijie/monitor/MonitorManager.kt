package com.shengshijie.monitor

import android.content.Context
import kotlin.concurrent.thread

object MonitorManager {

    private lateinit var mContext: Context
    private var start = true
    private var mInterval: Int = 5
    private var monitors = arrayListOf<Monitor>()
    var snapshot = mutableMapOf<String, String>()

    fun init(
        context: Context,
        interval: Int = 5,
        cpu: Boolean = true,
        memory: Boolean = true,
        network: Boolean = true
    ) {
        mContext = context
        mInterval = interval
        if (cpu) monitors.add(CpuMonitor())
        if (memory) monitors.add(MemoryMonitor())
        if (network) monitors.add(NetworkMonitor().apply { this.interval = interval })
    }

    fun startMonitor(f: ((MutableMap<String, String>) -> Unit)?) {
        start = true
        monitors.forEach { it.start() }
        thread {
            while (start) {
                val map = mutableMapOf<String, String>()
                monitors.forEach { map[it.javaClass.simpleName] = it.getInfo(mContext) }
                snapshot = map
                f?.invoke(map)
                Thread.sleep((mInterval * 1000).toLong())
            }
        }
    }

    fun stopMonitor() {
        start = false
        monitors.forEach { it.stop() }
    }

}