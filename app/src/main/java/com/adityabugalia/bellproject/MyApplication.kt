package com.adityabugalia.bellproject

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack

open class MyApplication: Application() {

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set

        private lateinit var requestQueue: RequestQueue

        fun getRequestQueue(): RequestQueue {
            return requestQueue
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        setUpNetworkCallApi()
    }

    private fun setUpNetworkCallApi() {
        VolleyLog.DEBUG = true

        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }
}