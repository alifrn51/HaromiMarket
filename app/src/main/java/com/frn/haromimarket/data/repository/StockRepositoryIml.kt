package com.frn.haromimarket.data.repository

import com.frn.haromimarket.data.csv.CSVParser
import com.frn.haromimarket.data.csv.CompanyListingsParser
import com.frn.haromimarket.data.local.StockDao
import com.frn.haromimarket.data.local.StockDatabase
import com.frn.haromimarket.data.mapper.toCompanyListing
import com.frn.haromimarket.data.mapper.toCompanyListingEntity
import com.frn.haromimarket.data.remote.StockApi
import com.frn.haromimarket.domain.model.CompanyListing
import com.frn.haromimarket.domain.repository.StockRepository
import com.frn.haromimarket.util.Resource
import com.opencsv.CSVReader
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryIml @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {

            emit(Resource.Loading<List<CompanyListing>>(true))
            val localListings = dao.searchCompanyListing(query = query)
            emit(Resource.Success<List<CompanyListing>>(data = localListings.map { it.toCompanyListing() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading<List<CompanyListing>>(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error<List<CompanyListing>>("Couldn't load data"))
                null

            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error<List<CompanyListing>>("Couldn't load data"))
                null
            }



            remoteListing?.let { listinigs ->
                dao.clearCompanyListing()
                dao.insertCompanyListings(
                    listinigs.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success<List<CompanyListing>>(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading<List<CompanyListing>>(false))
            }

        }

    }
}