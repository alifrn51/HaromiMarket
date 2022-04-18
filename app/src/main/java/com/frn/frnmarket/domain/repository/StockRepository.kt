package com.frn.frnmarket.domain.repository

import com.frn.frnmarket.domain.model.CompanyListing
import com.frn.frnmarket.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

}