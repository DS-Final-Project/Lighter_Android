package com.example.test_android2

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class LighterApplication : Application() {
    var userEmail: String? = null

    companion object {
        private var instance: LighterApplication? = null

        fun getInstance(): LighterApplication? = instance
        fun getEmail(): String {
            return instance?.userEmail ?: ""
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}