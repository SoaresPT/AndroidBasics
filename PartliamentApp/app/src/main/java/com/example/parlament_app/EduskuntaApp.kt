package com.example.parlament_app

import android.app.Application
import android.content.Context

class EduskuntaApp : Application() {

    init {
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
    }
}