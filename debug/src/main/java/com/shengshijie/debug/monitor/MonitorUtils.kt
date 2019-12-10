package com.shengshijie.debug.monitor

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.RandomAccessFile

object MonitorUtils {

    private var mProcessFile: RandomAccessFile? = null
    private var mAppFile: RandomAccessFile? = null
    private var mLastCpuTime: Long? = null
    private var mLastAppCpuTime: Long? = null

    fun cpuPercent(): Float {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cpuAboveO()
        } else {
            cpuUnderO()
        }
    }

    private fun cpuUnderO(): Float {
        val cpuTime: Long
        val appTime: Long
        var value = 0.0f
        try {
            if (mProcessFile == null || mAppFile == null) {
                mProcessFile = RandomAccessFile("/proc/stat", "r")
                mAppFile = RandomAccessFile("/proc/" + android.os.Process.myPid() + "/stat", "r")
            } else {
                mProcessFile!!.seek(0L)
                mAppFile!!.seek(0L)
            }
            val process = mProcessFile!!.readLine()
            val app = mAppFile!!.readLine()
            val processStatus = process.split(" ").toTypedArray()
            val appStatus = app.split(" ").toTypedArray()
            cpuTime = processStatus[2].toLong() + processStatus[3].toLong() + processStatus[4].toLong() + processStatus[5].toLong() + processStatus[6].toLong() + processStatus[7].toLong() + processStatus[8].toLong()
            appTime = appStatus[13].toLong() + appStatus[14].toLong()
            if (mLastCpuTime == null && mLastAppCpuTime == null) {
                mLastCpuTime = cpuTime
                mLastAppCpuTime = appTime
                return value
            }
            value =
                (appTime - mLastAppCpuTime!!).toFloat() / (cpuTime - mLastCpuTime!!).toFloat() * 100f
            mLastCpuTime = cpuTime
            mLastAppCpuTime = appTime
        } catch (e: Exception) {
            Log.e("", "cpuUnderO fail: $e")
        }
        return value
    }

    private fun cpuAboveO(): Float {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec("top -n 1")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String
            var cpuIndex = -1
            while (reader.readLine().also { line = it } != null) {
                line = line.trim { it <= ' ' }
                if (TextUtils.isEmpty(line)) {
                    continue
                }
                val tempIndex = cpuIndex(line)
                if (tempIndex != -1) {
                    cpuIndex = tempIndex
                    continue
                }
                if (line.startsWith(android.os.Process.myPid().toString())) {
                    if (cpuIndex == -1) {
                        continue
                    }
                    val param = line.split(("\\s+").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (param.size <= cpuIndex) {
                        continue
                    }
                    var cpu = param[cpuIndex]
                    if (cpu.endsWith("%")) {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"))
                    }
                    return cpu.toFloat() / Runtime.getRuntime().availableProcessors()
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
        } finally {
            process?.destroy()
        }
        return 0F
    }

    private fun cpuIndex(line: String): Int {
        if (line.contains("CPU")) {
            val titles = line.split(("\\s+").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in titles.indices) {
                if (titles[i].contains("CPU")) {
                    return i
                }
            }
        }
        return -1
    }

    fun memoryData(context: Context): Float {
        var mem = 0.0f
        try {
            val memInfo =
                (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getProcessMemoryInfo(
                    intArrayOf(android.os.Process.myPid())
                )
            if (memInfo.isNotEmpty()) {
                val totalPss = memInfo[0].totalPss
                if (totalPss >= 0) {
                    mem = totalPss * 1024.0f
                }
            }
        } catch (e: Exception) {
            Log.e("", "memoryData fail: $e")
        }
        return mem
    }
}