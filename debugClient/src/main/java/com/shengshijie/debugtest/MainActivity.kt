package com.shengshijie.debugtest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import com.shengshijie.debug.monitor.CpuMonitor
import com.shengshijie.debug.monitor.MemoryMonitor
import com.shengshijie.debug.monitor.MonitorManager
import com.shengshijie.debug.monitor.NetworkMonitor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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
        MonitorManager.init(this,interval = 1, upload = true, ip = "192.168.88.114", port = 8088) {
            runOnUiThread { tv_test.text = it }
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
}
