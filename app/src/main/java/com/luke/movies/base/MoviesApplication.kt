package com.luke.movies.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


const val FIRST_PAGE = 1
const val MOVIES_PER_PAGE = 20

@HiltAndroidApp
class MoviesApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}