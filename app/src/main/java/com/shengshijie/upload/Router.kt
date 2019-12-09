package com.shengshijie.upload

interface Router {

    fun getMethod(): String

    fun getPath(): String

    fun response(): String

}