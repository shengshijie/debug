package com.shengshijie.debug.upload

import com.shengshijie.debug.routers.HttpRouter
import java.io.*
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

object Client {

    private lateinit var bufferedWriter: BufferedWriter
    private lateinit var bufferedReader: BufferedReader

    fun start(ip: String, port: Int, f: (String) -> Unit) {
        thread {
            try {
                val socket = Socket(ip, port)
                bufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                while (true) {
                    val line = bufferedReader.readLine() ?: break
                    f.invoke(line)
                    println("receive:$line")
                    HttpRouter.parseRouter(bufferedWriter, line)
                }
            } catch (e: Exception) {
                f.invoke(e.message ?: "error")
            }
        }
//        while (true) {
//            request(Scanner(System.`in`).nextLine())
//        }
    }

    fun request(msg: String) {
        if(this::bufferedWriter.isInitialized){
            println("send:$msg")
            PrintWriter(bufferedWriter, true).println(msg)
        }else{
            println("not init:$msg")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        start("192.168.88.114", 8088) {}
    }

}