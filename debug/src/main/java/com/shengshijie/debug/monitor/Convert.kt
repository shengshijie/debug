package com.shengshijie.debug.monitor

fun byteUnitConvert(byteSize: Double): String {
    return when {
        byteSize < 0 -> {
            "shouldn't be less than zero!"
        }
        byteSize < K -> {
            String.format("%.2fB", byteSize)
        }
        byteSize < M -> {
            String.format("%.2fKB", byteSize / K)
        }
        byteSize < G -> {
            String.format("%.2fMB", byteSize / M)
        }
        else -> {
            String.format("%.2fGB", byteSize / G)
        }
    }
}

fun memParse(byteString: String): Double {
    return when {
        byteString.endsWith("KB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()/ K
        }
        byteString.endsWith("MB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()
        }
        byteString.endsWith("GB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * K
        }
        byteString.endsWith("B") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() / M
        }
        else -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()
        }
    }
}

fun netParse(byteString: String): Double {
    byteString.replace("/S","")
    return when {
        byteString.endsWith("KB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()
        }
        byteString.endsWith("MB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * K
        }
        byteString.endsWith("GB") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() * M
        }
        byteString.endsWith("B") -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble() / K
        }
        else -> {
            byteString.replace(Regex("[^\\d.]"), "").toDouble()
        }
    }
}

const val K = 1024
const val M = 1048576
const val G = 1072741824
