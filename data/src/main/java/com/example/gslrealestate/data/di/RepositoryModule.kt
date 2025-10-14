package com.example.gslrealestate.data.di

import com.example.gslrealestate.data.repository.ListingRepositoryImpl
import com.example.gslrealestate.domain.repository.ListingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing repository implementations
 * Following Dependency Inversion Principle - binds implementation to interface
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds ListingRepositoryImpl to ListingRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindListingRepository(
        listingRepositoryImpl: ListingRepositoryImpl
    ): ListingRepository
}

