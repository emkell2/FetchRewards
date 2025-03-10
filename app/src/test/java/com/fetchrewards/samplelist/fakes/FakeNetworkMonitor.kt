package com.fetchrewards.samplelist.fakes

import com.fetchrewards.samplelist.sortedlist.network.NetworkMonitor

class FakeNetworkMonitor : NetworkMonitor {
    var isNetworkConnectivityAvailableReturn = true
    override fun isNetworkConnectivityAvailable(): Boolean {
        return isNetworkConnectivityAvailableReturn
    }
}