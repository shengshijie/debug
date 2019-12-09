package com.shengshijie.debug

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import com.shengshijie.monitor.CpuMonitor
import com.shengshijie.monitor.MemoryMonitor
import com.shengshijie.monitor.MonitorManager
import com.shengshijie.monitor.NetworkMonitor
import com.shengshijie.upload.Client
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var run = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.setLogImpl(LogbackImpl().apply {
            file = true
            db = true
            socket = true
            socketHost = BuildConfig.HOST
            socketPort = BuildConfig.PORT
        })
        HLog.init(application, getExternalFilesDir(null)?.absolutePath, "RFT")
        MonitorManager.init(this)
        thread {
            Client.start{
                runOnUiThread {
                    tv_test.text = it
                }
            }
        }
    }

    fun test(view: View) {
        run = !run
        if (run) {
            MonitorManager.startMonitor {
                HLog.e("CPU", "${it[CpuMonitor::class.simpleName]}")
                HLog.e("MEM", "${it[MemoryMonitor::class.simpleName]}")
                HLog.e("NET", "${it[NetworkMonitor::class.simpleName]}")
            }
        } else {
            MonitorManager.stopMonitor()
        }
    }
}
