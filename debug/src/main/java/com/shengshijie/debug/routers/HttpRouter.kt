package com.shengshijie.debug.routers

import com.shengshijie.debug.upload.Router
import com.shengshijie.debug.upload.Utils
import java.io.BufferedWriter
import java.io.PrintWriter

object HttpRouter {

    private val mRouters: MutableList<Router> = mutableListOf()

    private fun registerRouters(routers: List<Router>) {
        mRouters.addAll(routers)
    }

    init {
        registerRouters(mutableListOf(DBListRouter(), MonitorRouter()))
    }

    fun parseRouter(bufferedWriter: BufferedWriter, line: String) {
        val printWriter = PrintWriter(bufferedWriter, true)
        if (line.startsWith("GET /")) {
            val start = line.indexOf('/') + 1
            val end = line.indexOf(' ', start)
            if (end < 0) {
                printWriter.println("HTTP/1.0 500 Internal Server Error")
                return
            }
            val route = line.substring(start, end)
            if (route.isEmpty()) {
                val response = "index.html"
                printWriter.println("HTTP/1.0 200 OK")
                printWriter.println("Content-Type: " + Utils.detectMimeType(route))
                printWriter.println("Content-Length: " + response.toByteArray().size)
                printWriter.println(response)
                return
            }
            if (route.startsWith("download")) {
                val response = "index.html"
                printWriter.println("HTTP/1.0 200 OK")
                printWriter.println("Content-Type: " + Utils.detectMimeType(route))
                printWriter.println(
                    "Content-Disposition: attachment; filename=${route.substring(
                        route.indexOf("=") + 1,
                        route.length
                    )}"
                )
                printWriter.println(response)
                return
            }
            val response = mRouters.firstOrNull { route.startsWith(it.getPath()) }?.response() ?: ""
            printWriter.println("HTTP/1.0 200 OK")
            printWriter.println("Content-Type: " + Utils.detectMimeType(route))
            printWriter.println("Content-Length: " + response.toByteArray().size)
            printWriter.println(response)
        }
    }

}