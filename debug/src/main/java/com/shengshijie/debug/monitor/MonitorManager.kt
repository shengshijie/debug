package com.shengshijie.debug.monitor

import android.content.Context
import com.google.gson.Gson
import com.shengshijie.debug.upload.Client
import java.util.*
import kotlin.concurrent.thread

object MonitorManager {

    private lateinit var mContext: Context
    private var start = true
    private var mUpload = true
    private var mInterval: Int = 5
    private var monitors = arrayListOf<Monitor>()
    var snapshot: MonitorInfo? = null

    fun init(
        context: Context,
        interval: Int = 5,
        cpu: Boolean = true,
        memory: Boolean = true,
        network: Boolean = true,
        upload: Boolean = true,
        ip: String = "",
        port: Int = 0,
        f: (String) -> Unit = {}
    ) {
        mContext = context
        mInterval = interval
        mUpload = upload
        if (cpu) monitors.add(CpuMonitor())
        if (memory) monitors.add(MemoryMonitor())
        if (network) monitors.add(NetworkMonitor().apply { this.interval = interval })
        if (mUpload) Client.start(ip, port, f)
    }

    fun startMonitor(f: ((MutableMap<String, String>) -> Unit)?) {
        start = true
        monitors.forEach { it.start() }
        thread {
            while (start) {
                val map = mutableMapOf<String, String>()
                monitors.forEach { map[it.javaClass.simpleName.toLowerCase(Locale.CHINA)] = it.getInfo(mContext) }
                snapshot = MonitorInfo(map)
                f?.invoke(map)
                if (mUpload) Client.request(Gson().toJson(snapshot?.map))
                Thread.sleep((mInterval * 1000).toLong())
            }
        }
    }

    fun stopMonitor() {
        start = false
        monitors.forEach { it.stop() }
    }

}