package com.example.mapwizetest

import android.app.Application;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

class MapwiseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = MapwizeConfiguration.Builder(this, "2bb1200b7088d24ca785ac53c9c34f0c").build()
        MapwizeConfiguration.start(config)
    }
}