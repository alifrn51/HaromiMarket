package com.frn.frnmarket.di

import com.frn.frnmarket.data.csv.CSVParser
import com.frn.frnmarket.data.csv.CompanyListingsParser
import com.frn.frnmarket.data.repository.StockRepositoryIml
import com.frn.frnmarket.domain.model.CompanyListing
import com.frn.frnmarket.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryIml: StockRepositoryIml
    ): StockRepository

}