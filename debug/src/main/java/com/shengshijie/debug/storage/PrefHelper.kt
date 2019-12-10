package com.shengshijie.debug.storage

import android.content.Context
import java.io.File
import java.util.*

private const val PREFS_SUFFIX = ".xml"
fun getSharedPreferenceTags(context: Context): List<String> {
    val tags = ArrayList<String>()
    val rootPath = context.applicationInfo.dataDir + "/shared_prefs"
    val root = File(rootPath)
    if (root.exists()) {
        for (file in root.listFiles()) {
            val fileName = file.name
            if (fileName.endsWith(PREFS_SUFFIX)) {
                tags.add(
                    fileName.substring(
                        0,
                        fileName.length - PREFS_SUFFIX.length
                    )
                )
            }
        }
    }
    tags.sort()
    return tags
}

fun getAllPrefTableName(context: Context): List<String> {
    return getSharedPreferenceTags(context)
}

fun getAllPrefData(
    context: Context,
    tag: String?
): Map<String, *> {
    val preferences =
        context.getSharedPreferences(tag, Context.MODE_PRIVATE)
    return preferences.all
}

fun addOrUpdateRow(context: Context, tableName: String?) {
    val preferences =
        context.getSharedPreferences(tableName, Context.MODE_PRIVATE)
    preferences.edit().putStringSet(tableName, null).apply()
}

fun deleteRow(context: Context, tableName: String?) {
    val preferences =
        context.getSharedPreferences(tableName, Context.MODE_PRIVATE)
    preferences.edit().remove(tableName).apply()
}
