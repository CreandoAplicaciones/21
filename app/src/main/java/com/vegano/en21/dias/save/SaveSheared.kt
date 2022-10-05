package com.vegano.en21.dias.save

import android.app.Application

class SaveSheared : Application() {

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()

        prefs = Prefs(applicationContext)
    }


}