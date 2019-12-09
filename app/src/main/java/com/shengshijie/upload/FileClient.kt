package com.shengshijie.upload

import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.net.Socket

fun main() {
    println("客户端运行...")
    upload("fxy.png") { result ->
        result.onSuccess {
            println("上传成功...")
        }.onFailure {
            println("上传失败..." + it.message)
        }
    }
}

fun upload(fileName: String, f: (Result<Unit>) -> Unit) {
    try {
        Socket("127.0.0.1", 8989).use { socket ->
            BufferedOutputStream(socket.getOutputStream()).use { bos ->
                FileInputStream(fileName).use { fis ->
                    fis.copyTo(bos)
                    f.invoke(Result.success(Unit))
                }
            }
        }
    } catch (e: Exception) {
        f.invoke(Result.failure(e))
    }
}