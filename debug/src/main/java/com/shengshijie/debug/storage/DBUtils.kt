package com.shengshijie.debug.storage

import android.content.Context
import android.util.Pair
import java.io.File
import java.text.MessageFormat
import java.util.*

fun getDatabaseFiles(context: Context): HashMap<String, Pair<File, String>>? {
    val databaseFiles =
        HashMap<String, Pair<File, String>>()
    try {
        for (databaseName in context.databaseList()) {
            val password: String = getDbPasswordFromStringResources(context, databaseName)
            databaseFiles[databaseName] = Pair(
                context.getDatabasePath(databaseName),
                password
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return databaseFiles


}

fun getDbPasswordFromStringResources(context: Context, name: String): String {
    var nameWithoutExt = name
    if (nameWithoutExt.endsWith(".db")) {
        nameWithoutExt = nameWithoutExt.substring(0, nameWithoutExt.lastIndexOf('.'))
    }
    val resourceName = MessageFormat.format("DB_PASSWORD_{0}", nameWithoutExt.toUpperCase())
    var password = ""
    val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
    if (resourceId != 0) {
        password = context.getString(resourceId)
    }
    return password
}