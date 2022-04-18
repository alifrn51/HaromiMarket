package com.frn.frnmarket.data.repository

import com.frn.frnmarket.data.csv.CSVParser
import com.frn.frnmarket.data.local.StockDatabase
import com.frn.frnmarket.data.mapper.toCompanyListing
import com.frn.frnmarket.data.mapper.toCompanyListingEntity
import com.frn.frnmarket.data.remote.StockApi
import com.frn.frnmarket.domain.model.CompanyListing
import com.frn.frnmarket.domain.repository.StockRepository
import com.frn.frnmarket.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryIml @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    val ccompanyListingsPareser: CSVParser<CompanyListing>
) : StockRepository {

    val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {

            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query = query)
            emit(Resource.Success(data = localListings.map { it.toCompanyListing() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {

                val response = api.getListings()
                ccompanyListingsPareser.parser(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data!"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data!"))
                null
            }

            remoteListings?.let { listings ->

                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )

                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))

            }

        }
    }
}