package com.shengshijie.debug.monitor

class MonitorInfo(val map: MutableMap<String, String>) {

    var cpumonitor: String by map
    var memorymonitor: String by map
    var networkmonitor: String by map

}

data class MonitorBean(
    var cpumonitor: String,
    var memorymonitor: String,
    var networkmonitor: String
)