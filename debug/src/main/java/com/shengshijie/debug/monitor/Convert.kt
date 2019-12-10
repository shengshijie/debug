package com.shengshijie.debug.monitor

fun byteUnitConvert(byteSize: Double): String {
    return when {
        byteSize < 0 -> {
            "shouldn't be less than zero!"
        }
        byteSize < KB -> {
            String.format("%.2fB", byteSize)
        }
        byteSize < MB -> {
            String.format("%.2fKB", byteSize / KB)
        }
        byteSize < GB -> {
            String.format("%.2fMB", byteSize / MB)
        }
        else -> {
            String.format("%.2fGB", byteSize / GB)
        }
    }
}

fun byteUnitParse(byteString: String): Double {
    return when {
        byteString.contains("KB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()
        }
        byteString.contains("MB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * KB
        }
        byteString.contains("GB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * MB
        }
        else -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * GB
        }
    }
}

const val BYTE = 1
const val KB = 1024
const val MB = 1048576
const val GB = 1072741824
