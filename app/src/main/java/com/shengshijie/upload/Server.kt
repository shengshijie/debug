package com.shengshijie.upload


import com.shengshijie.routers.HttpRouter
import java.io.*
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread

object Server {

    private val allOut = mutableMapOf<String, PrintWriter>()

    private val server = ServerSocket(8088)

    fun start() {
        while (true) {
            val socket = server.accept()
            val host = socket.inetAddress.hostAddress
            thread {
                val br = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
                val bw = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                synchronized(allOut) {
                    allOut.put(host, PrintWriter(bw, true))
                }
                println(host + " online: " + allOut.size)
                while (true) {
                    val line = br.readLine() ?: break
                    println("receive:$host:$line")
                    HttpRouter.parseRouter(bw, line)
                }
            }
            while (true) {
                request(host, Scanner(System.`in`).nextLine())
            }
        }
    }

    fun request(ip: String, msg: String) {
        println("send:$ip:$msg")
        allOut[ip]?.println(msg)
    }

    fun stop() {
        synchronized(allOut) {
            allOut.clear()
        }
        server.close()
    }

        @JvmStatic
        fun main(args: Array<String>) {
           start()
        }

}