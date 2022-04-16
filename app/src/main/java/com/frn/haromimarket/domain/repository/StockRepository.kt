package com.frn.haromimarket.domain.repository

import com.frn.haromimarket.domain.model.CompanyListing
import com.frn.haromimarket.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
       fetchFromRemote: Boolean,
       query: String
    ): Flow<Resource<List<CompanyListing>>>


}