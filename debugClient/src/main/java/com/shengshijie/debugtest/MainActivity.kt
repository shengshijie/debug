package com.shengshijie.debugtest

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.debug.monitor.CpuMonitor
import com.shengshijie.debug.monitor.MemoryMonitor
import com.shengshijie.debug.monitor.MonitorManager
import com.shengshijie.debug.monitor.NetworkMonitor
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {

    private var run = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.setLogImpl(LogbackImpl().apply {
            file = true
            db = true
        })
        HLog.init(application, getExternalFilesDir(null)?.absolutePath, "RFT")
        MonitorManager.init(this, interval = 1, upload = true, ip = "192.168.88.114", port = 8088) {

        }
    }

    fun test(view: View) {
        run = !run
        if (run) {
            MonitorManager.startMonitor {
                HLog.e("CPU", "${it[CpuMonitor::class.simpleName?.toLowerCase(Locale.CHINA)]}")
                HLog.e("MEM", "${it[MemoryMonitor::class.simpleName?.toLowerCase(Locale.CHINA)]}")
                HLog.e("NET", "${it[NetworkMonitor::class.simpleName?.toLowerCase(Locale.CHINA)]}")
            }
        } else {
            MonitorManager.stopMonitor()
        }
    }

    fun debug(view: View) {
        adb()
    }

    private fun adb() {
        val s = arrayOf("setprop service.adb.tcp.port 5555","stop adbd","start adbd")
        try {
            CommandUtils.execCommand(s, true)
            { Toast.makeText(this, "开启成功", Toast.LENGTH_SHORT).show() }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}
