package com.shengshijie.debug.routers

import com.shengshijie.debug.monitor.MonitorManager
import com.shengshijie.debug.upload.GetRouter

class MonitorRouter: GetRouter() {

    override fun getMethod(): String {
        return "GET /"
    }

    override fun getPath(): String {
        return "monitor"
    }

    override fun response(): String {
        return MonitorManager.snapshot.toString()
    }

}