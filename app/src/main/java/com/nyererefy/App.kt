package com.nyererefy

import android.app.Activity
import android.app.Service
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.nyererefy.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import javax.inject.Inject

class App : MultiDexApplication(), HasActivityInjector, HasServiceInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()
        appContext = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector() = dispatchingServiceInjector

    companion object {
        lateinit var appContext: Context
    }
}