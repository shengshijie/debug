package com.shengshijie.debug.upload


import com.shengshijie.debug.routers.HttpRouter
import java.io.*
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread

object Server {

    private val allOut = mutableMapOf<String, PrintWriter>()

    private lateinit var serverSocket: ServerSocket

    fun start(port: Int, f: (String) -> Unit) {
        serverSocket = ServerSocket(port)
        while (true) {
            val socket = serverSocket.accept()
            val host = socket.inetAddress.hostAddress
            thread {
                try {
                    val br = BufferedReader(
                        InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                        )
                    )
                    val bw = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                    synchronized(allOut) {
                        allOut.put(host, PrintWriter(bw, true))
                    }
                    println(host + " online: " + allOut.size)
                    while (true) {
                        val line = br.readLine() ?: break
                        println("receive:$host:$line")
                        f.invoke(line)
                        HttpRouter.parseRouter(bw, line)
                    }
                } catch (e: Exception) {
                    f.invoke(e.message ?: "error")
                }
            }
//            while (true) {
//                request(host, Scanner(System.`in`).nextLine())
//            }
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
        serverSocket.close()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        start(8088) {}
    }

}