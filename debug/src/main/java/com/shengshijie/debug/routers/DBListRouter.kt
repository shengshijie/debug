package com.shengshijie.debug.routers

import com.shengshijie.debug.upload.GetRouter

class DBListRouter: GetRouter() {

    override fun getMethod(): String {
        return "GET /"
    }

    override fun getPath(): String {
        return "getDbList"
    }

    override fun response(): String {
        return "{test,hah}"
    }

}