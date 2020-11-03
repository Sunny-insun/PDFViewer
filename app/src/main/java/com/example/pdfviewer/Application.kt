package com.example.pdfviewer

import android.app.Activity
import android.app.Application
import android.content.Context

class Application : Application() {
    companion object {
        lateinit var mActivity : Activity
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    fun context(): Context = applicationContext
}