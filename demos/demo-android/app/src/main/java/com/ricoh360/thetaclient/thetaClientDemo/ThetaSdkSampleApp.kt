package com.ricoh360.thetaclient.thetaClientDemo

import android.app.Application
import timber.log.Timber

class ThetaSdkSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}