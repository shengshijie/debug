package com.shengshijie.debug.upload

import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.ServerSocket

fun main() {
    println("服务器端运行...")
    download("fxy.png") { result ->
        result.onSuccess {
            println("下载成功...")
        }.onFailure {
            println("下载失败..." + it.message)
        }
    }
}

fun download(fileName: String, f: (Result<Unit>) -> Unit) {
    try {
        ServerSocket(8989).use { server ->
            server.accept().use { socket ->
                BufferedInputStream(socket.getInputStream()).use { bis ->
                    FileOutputStream(fileName).use { fos ->
                        bis.copyTo(fos)
                        f.invoke(Result.success(Unit))
                    }
                }
            }
        }
    } catch (e: Exception) {
        f.invoke(Result.failure(e))
    }
}