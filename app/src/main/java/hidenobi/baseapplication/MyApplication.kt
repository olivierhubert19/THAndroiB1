package hidenobi.baseapplication

import android.app.Application
import hidenobi.baseapplication.other.PreferenceHelper

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instances = this
        //init share preference singleton
        PreferenceHelper.customPreference(this)
    }

    companion object {
        lateinit var instances: MyApplication
    }
}