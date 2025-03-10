package com.fetchrewards.samplelist.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.fetchrewards.samplelist.sortedlist.network.DefaultNetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DefaultNetworkMonitorTest {
    private lateinit var subject: DefaultNetworkMonitor

    @ApplicationContext private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCapabilities: NetworkCapabilities

    @BeforeEach
    fun setup() {
        context = mock()
        connectivityManager = mock()
        networkCapabilities = mock()

        subject = DefaultNetworkMonitor(context)

        whenever(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(connectivityManager)

        whenever(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
            .thenReturn(networkCapabilities)
    }

    @Test
    fun `when network connectivity is detected, expect isNetworkConnectivityAvailable to return true `() {
        whenever(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(true)
        assertTrue(subject.isNetworkConnectivityAvailable())
    }

    @Test
    fun `when network connectivity is NOT detected, expect isNetworkConnectivityAvailable to return false `() {
        whenever(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(false)
        assertFalse(subject.isNetworkConnectivityAvailable())
    }
}