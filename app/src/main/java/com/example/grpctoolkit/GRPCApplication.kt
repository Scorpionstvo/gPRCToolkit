package com.example.grpctoolkit

import android.app.Application
import android.content.Context

class GRPCApplication : Application() {

    companion object {
        private lateinit var instance: GRPCApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}
