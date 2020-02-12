package com.shengshijie.debugtest

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeoutException

object CommandUtils {

    fun execCommand(
        commands: Array<String>,
        isRoot: Boolean,
        result: (CommandResult) -> Unit
    ) {
        var exitCode = -1
        if (commands.isEmpty()) {
            result.invoke(CommandResult(exitCode, null, null))
        }
        val successReader: BufferedReader
        val errorReader: BufferedReader
        val os: DataOutputStream
        val process: Process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
        os = DataOutputStream(process.outputStream)
        for (command in commands) {
            os.write(command.toByteArray())
            os.writeBytes("\n")
            os.flush()
        }
        os.writeBytes("exit\n")
        os.flush()
        exitCode = process.waitFor()
        val successMsg: StringBuilder = StringBuilder()
        val errorMsg: StringBuilder = StringBuilder()
        successReader = BufferedReader(InputStreamReader(process.inputStream))
        errorReader = BufferedReader(InputStreamReader(process.errorStream))
        var s: String?
        while (successReader.readLine().also { s = it } != null) {
            successMsg.append(s).append("\n")
        }
        while (errorReader.readLine().also { s = it } != null) {
            errorMsg.append(s).append("\n")
        }
        if (exitCode == -257) {
            throw TimeoutException()
        }
        try {
            os.close()
            successReader.close()
            errorReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        process.destroy()
        result.invoke(CommandResult(exitCode, successMsg.toString(), errorMsg.toString()))
    }

    data class CommandResult(
        private var exitCode: Int,
        private var successMsg: String?,
        private var errorMsg: String?
    )

}