package com.fetchrewards.samplelist.sortedlist.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Monitor network connectivity. Currently only checks for instantaneous connection but can be
 * expanded to observe connectivity changes.
 */
@Singleton
class DefaultNetworkMonitor @Inject constructor(
    @ApplicationContext private val  context: Context
) : NetworkMonitor {

    /**
     * Check network connectivity instantaneously.
     * @return if network connectivity is currently found.
     */
    override fun isNetworkConnectivityAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}