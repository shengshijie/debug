package com.shengshijie.routers

import com.shengshijie.monitor.MonitorManager
import com.shengshijie.upload.GetRouter

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