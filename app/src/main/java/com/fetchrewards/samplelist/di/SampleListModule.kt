package com.fetchrewards.samplelist.di

import com.fetchrewards.samplelist.sortedlist.data.repository.DefaultItemsRepository
import com.fetchrewards.samplelist.sortedlist.data.repository.ItemsRepository
import com.fetchrewards.samplelist.sortedlist.network.DefaultNetworkMonitor
import com.fetchrewards.samplelist.sortedlist.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object SampleListModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface Bindings {
        @Singleton
        @Binds
        fun bindsItemsRepository(repo: DefaultItemsRepository): ItemsRepository

        @Singleton
        @Binds
        fun bindNetworkMonitor(monitor: DefaultNetworkMonitor) : NetworkMonitor
    }
}