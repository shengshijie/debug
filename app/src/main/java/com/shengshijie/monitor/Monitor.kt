package com.shengshijie.monitor

import android.content.Context

interface Monitor {

    fun start()

    fun getInfo(context: Context): String

    fun stop()

}