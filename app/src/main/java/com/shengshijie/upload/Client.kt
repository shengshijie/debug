package com.shengshijie.upload

import com.shengshijie.routers.HttpRouter
import java.io.*
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

object Client {

    private lateinit var bufferedWriter: BufferedWriter
    private lateinit var bufferedReader: BufferedReader

    fun start(f: (String) -> Unit) {
        val socket = Socket("192.168.88.186", 8088)
        bufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        thread {
            while (true) {
                val line = bufferedReader.readLine() ?: break
                f.invoke(line)
                println("receive:$line")
                HttpRouter.parseRouter(bufferedWriter, line)
            }
        }
//        while (true) {
//            request(Scanner(System.`in`).nextLine())
//        }
    }

    fun request(msg: String) {
        println("send:$msg")
        PrintWriter(bufferedWriter, true).println(msg)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        start {}
    }

}